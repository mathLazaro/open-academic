package com.github.open_academic_ad_hoc.model.dto.select;

public enum WorkOrganizationSelect implements Select {
    ROLE_TYPE("role", String.class);

    private final String attribute;

    private final Class<?> type;

    WorkOrganizationSelect(String attribute, Class<?> type) {

        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() {
        return attribute;
    }

    public Class<?> type() { return type; }

}
