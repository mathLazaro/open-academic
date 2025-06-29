package com.github.open_academic_ad_hoc.model.dto;

import com.github.open_academic_ad_hoc.model.dto.table.Table;

import java.util.Set;

public record QueryBuilderDTO(
        Table root,
        Set<JoinDTO> joinSet,
        Set<ColumnDTO> columnSet,
        Set<WhereDTO> whereSet,
        GroupByDTO groupBy
) {

}
