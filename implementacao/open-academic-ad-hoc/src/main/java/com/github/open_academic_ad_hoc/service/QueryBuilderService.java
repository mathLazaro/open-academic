package com.github.open_academic_ad_hoc.service;

import com.github.open_academic_ad_hoc.model.Selectable;
import com.github.open_academic_ad_hoc.model.dto.*;
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

import java.time.LocalDate;
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

        Table rootTable = request.root();
        GroupByDTO groupBy = request.groupBy();

        // gera os joins
        Map<Table, From<?, ?>> joins = new HashMap<>();
        Root<? extends Selectable> root = query.from(rootTable.toSelectable());
        joins.put(rootTable, root);
        prepareJoinTables(rootTable, joins, request.joinSet());


        // mapeia as colunas selecionadas
        if (Objects.isNull(groupBy)) {
            List<Selection<Object>> fields = prepareSelectStatements(request.columnSet(), joins);
            query.multiselect(fields.toArray(new Selection[0]));
        } else {
            // processa groupBy
            prepareGroupByStatements(request.groupBy(), joins, query);
        }


        // adiciona os parâmetros where
        if (Objects.nonNull(request.whereSet()) && !request.whereSet().isEmpty()) {
            List<Predicate> predicates = prepareWhereStatements(request.whereSet(), joins);
            query.where(predicates.toArray(new Predicate[0]));
        }

        // formata os resultados
        TypedQuery<Tuple> jpaQuery = entityManager.createQuery(query);
        List<Tuple> resultList = jpaQuery.getResultList();

        String sql = SQLExtractor.from(jpaQuery);
        List<Map<String, Object>> result = mapResultsToFriendlyFormat(resultList);

        return Pair.of(sql, result);
    }

    private void prepareGroupByStatements(GroupByDTO groupBy, Map<Table, From<?, ?>> joins, CriteriaQuery<Tuple> query) {

        if (Objects.nonNull(groupBy)) {
            List<Selection<Object>> groupByList = prepareSelectStatements(groupBy.columnSet(), joins);
            List<Selection<?>> selections = new ArrayList<>(groupByList);

            AggregationDTO aggregation = groupBy.aggregation();
            if (Objects.nonNull(aggregation)) {
                Selection<?> selection = prepareAggregation(aggregation, joins);
                selections.add(selection);
            }

            query.groupBy(groupByList.toArray(new Expression[0]));
            query.multiselect(selections);
        }
    }

    private Selection<?> prepareAggregation(
            AggregationDTO aggregation,
            Map<Table, From<?, ?>> joins
    ) {

        Table table = aggregation.table();
        Select field = aggregation.field();
        String alias = aggregation.alias();
        From<?, ?> from = joins.get(table);

        if (Objects.isNull(alias) || alias.isEmpty()) {
            alias = table.attribute() + "_" + field.attribute() + "_" + aggregation.aggregation();
        }

        // Exemplo: agregação SUM

        Expression<? extends Number> path = (Expression<? extends Number>) getPath(from, field.attribute());

        return switch (aggregation.aggregation()) {
            case AVG -> cb.avg(path).alias(alias);
            case MAX -> cb.max(path).alias(alias);
            case MIN -> cb.min(path).alias(alias);
            case SUM -> cb.sum(path).alias(alias);
            case COUNT -> cb.count(path).alias(alias);
            case COUNT_DISTINCT -> cb.countDistinct(path).alias(alias);
        };
    }

    private static List<Map<String, Object>> mapResultsToFriendlyFormat(List<Tuple> resultList) {

        return resultList
                .stream()
                .map(tuple -> {
                    Map<String, Object> linha = new HashMap<>();
                    for (TupleElement<?> el : tuple.getElements()) {
                        linha.put(el.getAlias(), tuple.get(el));
                    }
                    return linha;
                }).toList();
    }

    private List<Predicate> prepareWhereStatements(
            Set<WhereDTO> whereSet,
            Map<Table, From<?, ?>> joins
    ) {

        final List<Predicate> predicates = new ArrayList<>();
        whereSet.forEach(whereDTO -> {
            Table table = whereDTO.table();
            Select field = whereDTO.field();
            Operator operator = whereDTO.operator();
            Object value = field.castValue(whereDTO.value());
            String attribute = field.attribute();

            var from = joins.get(table);
            Expression<?> path = getPath(from, attribute);

            Predicate predicate = switch (value) {
                case Double v -> getPredicateFilter(v, operator, path.as(Double.class));
                case Integer i -> getPredicateFilter(i, operator, path.as(Integer.class));
                case Long l -> getPredicateFilter(l, operator, path.as(Long.class));
                case LocalDate localDate -> getPredicateFilter(localDate, operator, path.as(LocalDate.class));
                case null, default -> getPredicateFilter((String) value, operator, path.as(String.class));
            };

            if (Objects.nonNull(predicate)) {
                predicates.add(predicate);
            }
        });
        return predicates;
    }

    private <T extends Comparable<? super T>> Predicate getPredicateFilter(T value, Operator operator, Expression<T> path) {

        return switch (operator) {
            case GREATER_THAN -> cb.greaterThan(path, value);
            case GREATER_THAN_EQUALS -> cb.greaterThanOrEqualTo(path, value);
            case LESS_THAN -> cb.lessThan(path, value);
            case LESS_THAN_EQUALS -> cb.lessThanOrEqualTo(path, value);
            case EQUALS -> cb.equal(path, value);
            case NOT_EQUALS -> cb.notEqual(path, value);
            case LIKE -> cb.like(path.as(String.class), "%" + value.toString() + "%");
        };
    }

    private static Expression<?> getPath(From<?, ?> from, String attribute) {

        Path<?> path;
        try {
            path = from.get(attribute);
        } catch (Exception e) {
            path = from.get("id").get(attribute);
        }
        return path;
    }


    private static List<Selection<Object>> prepareSelectStatements(
            Set<ColumnDTO> columnSet,
            Map<Table, From<?, ?>> joins
    ) {

        List<Selection<Object>> fields;
        if (!columnSet.isEmpty()) {
            fields = columnSet
                    .stream()
                    .map(col -> {
                        Table table = col.table();
                        Select field = col.field();
                        String alias = col.alias();

                        if (alias.isEmpty()) {
                            alias = table.attribute() + "_" + field.attribute();
                        }

                        return defineSelectByTable(table, field, alias, joins);
                    })
                    .toList();
        } else {
            fields = joins.keySet()
                    .stream()
                    .map(table -> Select.getFromTable(table)
                            .stream()
                            .map(field -> {
                                String alias = table.attribute() + "_" + field.attribute();
                                return defineSelectByTable(table, field, alias, joins);
                            })
                            .toList()
                    )
                    .flatMap(Collection::stream)
                    .toList();
        }
        return fields;
    }

    private static void prepareJoinTables(
            Table reference,
            Map<Table, From<?, ?>> joins,
            Set<JoinDTO> joinDTOSet
    ) {

        if (!joins.containsKey(reference)) {
            return;
        }

        joinDTOSet.stream().filter(join -> join.from().equals(reference)).forEach(join -> {
            From<?, ?> from = joins.get(join.from());
            joins.put(join.to(), from.join(join.to().attribute(), join.type()));
            prepareJoinTables(join.to(), joins, joinDTOSet);
        });
    }

    private static Selection<Object> defineSelectByTable(Table table, Select field, String alias, Map<Table, From<?, ?>> joins) {

        var from = joins.get(table);

        switch (table) {
            case ROLE -> {
                if (field.equals(RoleSelect.ROLE)) {
                    return selectFromIdEmbedded(from, field, alias);
                } else {
                    return selectDefault(from, field, alias);
                }
            }
            case WORK_ORGANIZATION -> {
                return selectFromIdEmbedded(from, field, alias);
            }
            default -> {
                return selectDefault(from, field, alias);
            }
        }
    }

    private static Selection<Object> selectFromIdEmbedded(
            From<?, ?> from,
            Select field,
            String alias
    ) {

        String attribute = field.attribute();
        var embeddedId = from.get("id");
        var select = embeddedId.get(attribute);

        if (Objects.nonNull(alias)) {
            return select.alias(alias);
        } else {
            return select;
        }
    }

    private static Selection<Object> selectDefault(
            From<?, ?> from,
            Select field,
            String alias
    ) {

        Path<Object> select = from.get(field.attribute());

        if (Objects.nonNull(alias)) {
            return select.alias(alias);
        } else {
            return select;
        }
    }

}
