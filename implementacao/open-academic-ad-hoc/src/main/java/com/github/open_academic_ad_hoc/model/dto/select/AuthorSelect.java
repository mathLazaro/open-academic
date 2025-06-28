package com.github.open_academic_ad_hoc.model.dto.select;

import java.util.List;

public enum AuthorSelect implements Select {
    ID("id", String.class),
    NAME("name", String.class),
    WORKS_COUNT("worksCount", Integer.class),
    CITED_BY_COUNT("citedByCount", Integer.class),;

    private final String attribute;

    private final Class<?> type;

    AuthorSelect(String attribute, Class<?> type) {

        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() {
        return attribute;
    }

    public Class<?> type() { return type; }

}
