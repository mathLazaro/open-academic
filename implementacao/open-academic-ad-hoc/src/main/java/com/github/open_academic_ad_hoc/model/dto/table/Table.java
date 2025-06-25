package com.github.open_academic_ad_hoc.model.dto.table;

import com.github.open_academic_ad_hoc.model.Selectable;
import com.github.open_academic_ad_hoc.model.main.*;
import com.github.open_academic_ad_hoc.model.relation.Authorship;
import com.github.open_academic_ad_hoc.model.relation.WorkOrganization;
import com.github.open_academic_ad_hoc.model.relation.WorkTopic;

public enum Table {
    AUTHOR("author") ,
    DOMAIN("domain"),
    FIELD("field"),
    ORGANIZATION("organization"),
    ROLE("role"),
    TOPIC("topic"),
    WORK("work"),
    AUTHORSHIP("authorship"),
    WORK_ORGANIZATION("workOrganization"),
    WORK_TOPIC("workTopic"),;

    Table(String attribute) {
        this.attribute = attribute;
    }

    private final String attribute;

    public String attribute() {
        return attribute;
    }

    public Class<? extends Selectable> toSelectable() {

        return switch (this) {
            case AUTHOR -> Author.class;
            case DOMAIN -> Domain.class;
            case FIELD -> Field.class;
            case ORGANIZATION -> Organization.class;
            case ROLE -> Role.class;
            case TOPIC -> Topic.class;
            case WORK -> Work.class;
            case AUTHORSHIP -> Authorship.class;
            case WORK_ORGANIZATION -> WorkOrganization.class;
            case WORK_TOPIC -> WorkTopic.class;
        };
    }
}
