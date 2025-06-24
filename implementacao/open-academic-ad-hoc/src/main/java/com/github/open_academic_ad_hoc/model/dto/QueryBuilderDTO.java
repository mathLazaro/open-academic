package com.github.open_academic_ad_hoc.model.dto;

import jakarta.persistence.criteria.JoinType;

import java.util.Map;
import java.util.Set;

public record QueryBuilderDTO(
        Table root,
        Map<Table, JoinType> joins,
        Set<Column> columns
        // TODO where
        // TODO order by
        // TODO group by
        // TODO having
) {

}
