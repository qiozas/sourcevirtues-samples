package com.sourcevirtues.etl.storm.morphlines.mapper.result;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonNode2StringResultMapper implements IRecordResultMapper<JsonNode, String> {
    private static final long serialVersionUID = 1L;

    public String map(JsonNode value) {
        return value == null ? null : value.toString();
    }
}
