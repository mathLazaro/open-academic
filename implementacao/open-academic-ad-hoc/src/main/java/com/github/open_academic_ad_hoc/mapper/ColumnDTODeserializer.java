package com.github.open_academic_ad_hoc.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.open_academic_ad_hoc.model.dto.ColumnDTO;
import com.github.open_academic_ad_hoc.model.dto.select.*;
import com.github.open_academic_ad_hoc.model.dto.table.Table;

import java.io.IOException;

public class ColumnDTODeserializer extends GenericDeserializer<ColumnDTO> {

    public ColumnDTODeserializer() {

        super(ColumnDTO.class);
    }

    @Override
    public ColumnDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = mapper.readTree(p);

        Table table = Table.valueOf(node.get("table").asText());

        String fieldStr = node.get("field").asText();
        String alias = node.has("alias") ? node.get("alias").asText() : null;
        Select field = convertField(table, fieldStr);

        return new ColumnDTO(table, field, alias);
    }
}
