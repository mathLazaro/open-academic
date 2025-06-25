package com.github.open_academic_ad_hoc.model.dto.select;

public enum WorkTopicSelect implements Select {
    SCORE("score", Double.class);

    private final String attribute;

    private final Class<?> type;

    WorkTopicSelect(String attribute, Class<?> type) {

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
