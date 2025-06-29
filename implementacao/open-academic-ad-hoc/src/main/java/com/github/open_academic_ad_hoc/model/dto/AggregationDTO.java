package com.github.open_academic_ad_hoc.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.open_academic_ad_hoc.mapper.AggregationDTODeserializer;
import com.github.open_academic_ad_hoc.model.dto.agg.Aggregation;
import com.github.open_academic_ad_hoc.model.dto.select.Select;
import com.github.open_academic_ad_hoc.model.dto.table.Table;

@JsonDeserialize(using = AggregationDTODeserializer.class)
public record AggregationDTO(
        Table table,
        Select field,
        Aggregation aggregation,
        String alias
) {

}
