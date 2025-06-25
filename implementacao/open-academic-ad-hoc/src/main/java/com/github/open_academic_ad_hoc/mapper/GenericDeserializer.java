package com.github.open_academic_ad_hoc.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.open_academic_ad_hoc.model.dto.select.*;
import com.github.open_academic_ad_hoc.model.dto.table.Table;

import java.io.IOException;

public abstract class GenericDeserializer<T> extends StdDeserializer<T> {

    public Class<T> clazz;

    public GenericDeserializer(Class<T> clazz) {

        super(clazz);
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        return null;
    }

    protected Select convertField(Table table, String fieldStr) {
        Select field;

        switch (table) {
            case AUTHOR -> field = AuthorSelect.valueOf(fieldStr);
            case DOMAIN -> field = DomainSelect.valueOf(fieldStr);
            case FIELD -> field = FieldSelect.valueOf(fieldStr);
            case ORGANIZATION -> field = OrganizationSelect.valueOf(fieldStr);
            case ROLE -> field = RoleSelect.valueOf(fieldStr);
            case TOPIC -> field = TopicSelect.valueOf(fieldStr);
            case WORK -> field = WorkSelect.valueOf(fieldStr);
            case WORK_ORGANIZATION -> field = WorkOrganizationSelect.valueOf(fieldStr);
            case WORK_TOPIC -> field = WorkTopicSelect.valueOf(fieldStr);
            default -> throw new IllegalArgumentException("Tabela não suportada: " + table);
        }

        return  field;
    }

}
