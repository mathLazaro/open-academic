package com.github.open_academic_ad_hoc.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.open_academic_ad_hoc.mapper.ColumnDTODeserializer;
import com.github.open_academic_ad_hoc.model.dto.select.Select;
import com.github.open_academic_ad_hoc.model.dto.table.Table;


@JsonDeserialize(using = ColumnDTODeserializer.class)
public record ColumnDTO(
        Table table,
        Select field,
        String alias
) {

}
