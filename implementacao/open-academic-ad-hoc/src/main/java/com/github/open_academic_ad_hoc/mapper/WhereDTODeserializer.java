package com.github.open_academic_ad_hoc.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.open_academic_ad_hoc.model.WhereDTO;
import com.github.open_academic_ad_hoc.model.dto.ColumnDTO;
import com.github.open_academic_ad_hoc.model.dto.QueryBuilderDTO;
import com.github.open_academic_ad_hoc.model.dto.select.*;
import com.github.open_academic_ad_hoc.model.dto.table.Table;
import com.github.open_academic_ad_hoc.model.dto.where.Operator;

import java.io.IOException;

public class WhereDTODeserializer extends GenericDeserializer<WhereDTO> {

    public WhereDTODeserializer() {

        super(WhereDTO.class);
    }

    @Override
    public WhereDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = mapper.readTree(p);

        Table table = Table.valueOf(node.get("table").asText());

        String fieldStr = node.get("field").asText();
        Operator operator = Operator.valueOf(node.get("operator").asText());
        String value = node.get("value").asText();
        Select field = convertField(table, fieldStr);

        return new WhereDTO(table, field, operator, value);
    }
}
