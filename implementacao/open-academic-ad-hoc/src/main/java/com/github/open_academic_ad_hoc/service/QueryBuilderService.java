package com.github.open_academic_ad_hoc.service;

import com.github.open_academic_ad_hoc.model.Selectable;
import com.github.open_academic_ad_hoc.model.dto.JoinDTO;
import com.github.open_academic_ad_hoc.model.dto.QueryBuilderDTO;
import com.github.open_academic_ad_hoc.model.dto.select.RoleSelect;
import com.github.open_academic_ad_hoc.model.dto.select.Select;
import com.github.open_academic_ad_hoc.model.dto.table.Table;
import com.github.open_academic_ad_hoc.model.dto.where.Operator;
import io.hypersistence.utils.hibernate.query.SQLExtractor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueryBuilderService {

    private final EntityManager entityManager;

    private final CriteriaBuilder cb;

    public QueryBuilderService(EntityManager entityManager) {

        this.entityManager = entityManager;
        this.cb = entityManager.getCriteriaBuilder();
    }

    public Pair<String, Object> generateReport(QueryBuilderDTO request) {

        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        // gera os joins
        Map<Table, From<?, ?>> joins = new HashMap<>();
        Root<? extends Selectable> root = query.from(request.root().toSelectable());
        joins.put(request.root(), root);
        makeJoins(request.root(), joins, request.joinSet());


        // mapeia as colunas selecionadas
        if (!request.columnSet().isEmpty()) {
            List<Selection<Object>> fields = request.columnSet()
                    .stream()
                    .map(col -> {
                        Table table = col.table();
                        Select field = col.field();
                        String alias = col.alias();

                        return defineSelect(table, field, alias, joins);
                    })
                    .toList();
            query.multiselect(fields.toArray(new Selection[0]));
        } else {
            List<Selection<Object>> fields = joins.keySet()
                    .stream()
                    .map(table -> Select.getFromTable(table)
                            .stream()
                            .map(field -> {
                                String alias = field.attribute();
                                if (field.type() == String.class) {
                                    alias = table.attribute() + "_" + field.attribute();
                                }
                                return defineSelect(table, field, alias, joins);
                            })
                            .toList()
                    )
                    .flatMap(Collection::stream)
                    .toList();
            query.multiselect(fields.toArray(new Selection[0]));
        }


        // adiciona os parâmetros where
        if (Objects.nonNull(request.whereSet()) && !request.whereSet().isEmpty()) {
            final List<Predicate> predicates = new ArrayList<>();
            request.whereSet()
                    .forEach(whereDTO -> {
                        Table table = whereDTO.table();
                        Select field = whereDTO.field();
                        Operator operator = whereDTO.operator();
                        Object value = field.castValue(whereDTO.value());

                        var from = joins.get(table);
                        var path = from.get(field.attribute());

                        Predicate predicate = switch (operator) {
                            case EQUALS -> cb.equal(path, value);
                            case NOT_EQUALS -> cb.notEqual(path, value);
                            case GREATER_THAN -> cb.greaterThan(path.as(Comparable.class), (Comparable) value);
                            case GREATER_THAN_EQUALS ->
                                    cb.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) value);
                            case LESS_THAN -> cb.lessThan(path.as(Comparable.class), (Comparable) value);
                            case LESS_THAN_EQUALS ->
                                    cb.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) value);
                            case LIKE -> cb.like(path.as(String.class), "%" + value + "%");
                        };

                        if (predicate != null) {
                            predicates.add(predicate);
                        }
                    });
            query.where(predicates.toArray(new Predicate[0]));
        }

        TypedQuery<Tuple> jpaQuery = entityManager.createQuery(query);
        List<Tuple> resultList = jpaQuery.getResultList();

        String sql = SQLExtractor.from(jpaQuery);

        List<Map<String, Object>> result = resultList
                .stream()
                .map(tuple -> {
                    Map<String, Object> linha = new HashMap<>();
                    for (TupleElement<?> el : tuple.getElements()) {
                        linha.put(el.getAlias(), tuple.get(el));
                    }
                    return linha;
                }).toList();

        return Pair.of(sql, result);
    }

    private static void makeJoins(Table reference, Map<Table, From<?, ?>> joins, Set<JoinDTO> joinDTOSet) {

        if (!joins.containsKey(reference)) {
            return;
        }

        joinDTOSet.stream().filter(join -> join.from().equals(reference)).forEach(join -> {
            From<?, ?> from = joins.get(join.from());
            joins.put(join.to(), from.join(join.to().attribute(), join.type()));
            makeJoins(join.to(), joins, joinDTOSet);
        });
    }

    private static Selection<Object> defineSelect(Table table, Select field, String alias, Map<Table, From<?, ?>> joins) {

        var from = joins.get(table);

        switch (table) {
            case ROLE -> {
                if (field.equals(RoleSelect.ROLE)) {
                    return selectFromIdEmbedded(from, field, alias, table);
                } else {
                    return selectDefault(from, field, alias, table);
                }
            }
            case WORK_ORGANIZATION -> {
                return selectFromIdEmbedded(from, field, alias, table);
            }
            default -> {
                return selectDefault(from, field, alias, table);
            }
        }
    }

    private static Selection<Object> selectFromIdEmbedded(
            From<?, ?> from,
            Select field,
            String alias,
            Table table
    ) {

        String attribute = field.attribute();
        var embeddedId = from.get("id");
        var select = embeddedId.get(attribute);

        return select
                .alias(alias.isBlank()
                        ? table.attribute() + "_" + attribute
                        : alias
                );
    }

    private static Selection<Object> selectDefault(
            From<?, ?> from,
            Select field,
            String alias,
            Table table
    ) {

        return from
                .get(field.attribute())
                .alias(alias.isBlank()
                        ? table.attribute() + "_" + field.attribute()
                        : alias
                );
    }

}
