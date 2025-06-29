package com.github.open_academic_ad_hoc.model.dto;

import java.util.Set;

public record GroupByDTO(
        Set<ColumnDTO> columnSet,
        AggregationDTO aggregation
) {

}
