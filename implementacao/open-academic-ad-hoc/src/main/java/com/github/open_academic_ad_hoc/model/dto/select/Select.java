package com.github.open_academic_ad_hoc.model.dto.select;

import java.time.LocalDate;
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
}
