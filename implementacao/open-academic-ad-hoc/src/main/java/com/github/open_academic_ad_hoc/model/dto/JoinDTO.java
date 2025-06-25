package com.github.open_academic_ad_hoc.model.dto;

import com.github.open_academic_ad_hoc.model.dto.table.Table;
import jakarta.persistence.criteria.JoinType;

public record JoinDTO(
        Table from,
        Table to,
        JoinType type
) {


}
