package com.github.open_academic_ad_hoc.model.dto.select;

public enum TopicSelect implements Select {
    ID("id", String.class),
    TITLE("title", String.class),
    DESCRIPTION("description", String.class),
    WORKS_COUNT("worksCount", Integer.class),;

    private final String attribute;

    private final Class<?> type;

    TopicSelect(String attribute, Class<?> type) {

        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() {
        return attribute;
    }

    public Class<?> type() { return type; }
}
