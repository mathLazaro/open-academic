package com.github.open_academic_ad_hoc.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.open_academic_ad_hoc.mapper.WhereDTODeserializer;
import com.github.open_academic_ad_hoc.model.dto.select.Select;
import com.github.open_academic_ad_hoc.model.dto.table.Table;
import com.github.open_academic_ad_hoc.model.dto.where.Operator;

@JsonDeserialize(using = WhereDTODeserializer.class)
public record WhereDTO(
        Table table,
        Select field,
        Operator operator,
        String value
) {
}
