package com.github.open_academic_ad_hoc.model.dto;

import com.github.open_academic_ad_hoc.model.dto.select.Select;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Column {

    private Table table;

    private Select field;

    private String alias;

}
