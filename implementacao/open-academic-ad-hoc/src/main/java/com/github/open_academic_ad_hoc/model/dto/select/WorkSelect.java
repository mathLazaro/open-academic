package com.github.open_academic_ad_hoc.model.dto.select;

import java.time.LocalDate;

public enum WorkSelect implements Select {
    ID("id", String.class),
    TITLE("title", String.class),
    IS_OPEN("isOpen", Boolean.class),
    REFERENCED_WORKS_COUNT("referencedWorksCount", Integer.class),
    CITED_BY_COUNT("citedByCount", Integer.class),
    FWCI("fwci", Double.class),
    PUBLISH_DATE("publishDate", LocalDate.class),
    TYPE("type", String.class);

    private final String attribute;

    private final Class<?> type;

    WorkSelect(String attribute, Class<?> type) {

        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() {

        return attribute;
    }

    public Class<?> type() {

        return type;
    }
}
