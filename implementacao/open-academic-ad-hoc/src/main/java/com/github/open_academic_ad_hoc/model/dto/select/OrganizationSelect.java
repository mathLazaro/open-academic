package com.github.open_academic_ad_hoc.model.dto.select;

public enum OrganizationSelect implements Select {
    ID("id", String.class),
    NAME("name", String.class),
    CITY("city", String.class),
    COUNTRY("country", String.class),
    COUNTRY_CODE("countryCode", String.class),
    WORKS_COUNT("worksCount", Integer.class),
    CITED_BY_COUNT("citedByCount", Integer.class),;

    private final String attribute;

    private final Class<?> type;

    OrganizationSelect(String attribute, Class<?> type) {

        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() {
        return attribute;
    }

    public Class<?> type() { return type; }
}
