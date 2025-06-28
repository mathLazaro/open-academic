package com.github.open_academic_ad_hoc.model.dto.select;

import com.github.open_academic_ad_hoc.model.dto.table.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface Select {

    String attribute();

    Class<?> type();

    default Object castValue(String raw) {

        if (raw == null) return null;

        Class<?> t = type();

        if (t == String.class) return raw;
        if (t == Integer.class) return Integer.parseInt(raw);
        if (t == Long.class) return Long.parseLong(raw);
        if (t == Boolean.class) return Boolean.parseBoolean(raw);
        if (Objects.equals(t, LocalDate.class)) return LocalDate.parse(raw);

        throw new IllegalArgumentException("Não é possível converter " + raw + " para " + t.getSimpleName());
    }

    static List<Select> getFromTable(Table table) {

        return switch (table) {
            case AUTHOR -> List.of(AuthorSelect.values());
            case DOMAIN -> List.of(DomainSelect.values());
            case FIELD -> List.of(FieldSelect.values());
            case ORGANIZATION -> List.of(OrganizationSelect.values());
            case ROLE -> List.of(RoleSelect.values());
            case TOPIC -> List.of(TopicSelect.values());
            case WORK -> List.of(WorkSelect.values());
            case WORK_ORGANIZATION -> List.of(WorkOrganizationSelect.values());
            case WORK_TOPIC -> List.of(WorkTopicSelect.values());
            default -> List.of();
        };
    }
}
