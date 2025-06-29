package com.github.open_academic_ad_hoc.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.open_academic_ad_hoc.model.dto.AggregationDTO;
import com.github.open_academic_ad_hoc.model.dto.ColumnDTO;
import com.github.open_academic_ad_hoc.model.dto.agg.Aggregation;
import com.github.open_academic_ad_hoc.model.dto.select.Select;
import com.github.open_academic_ad_hoc.model.dto.table.Table;

import java.io.IOException;

public class AggregationDTODeserializer extends GenericDeserializer<AggregationDTO> {

    public AggregationDTODeserializer() {

        super(AggregationDTO.class);
    }

    @Override
    public AggregationDTO deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        JsonNode node = mapper.readTree(p);

        Table table = Table.valueOf(node.get("table").asText());

        String fieldStr = node.get("field").asText();
        String alias = node.has("alias") ? node.get("alias").asText() : null;
        Select field = convertField(table, fieldStr);
        Aggregation aggregation = Aggregation.valueOf(node.get("aggregation").asText());

        return new AggregationDTO(table, field, aggregation, alias);
    }


}
