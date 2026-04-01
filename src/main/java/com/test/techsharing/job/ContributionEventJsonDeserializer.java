package com.test.techsharing.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.techsharing.model.ContributionEvent;
import com.test.techsharing.util.JsonMapperFactory;
import org.apache.flink.api.common.functions.MapFunction;

public class ContributionEventJsonDeserializer implements MapFunction<String, ContributionEvent> {

    private static final ObjectMapper OBJECT_MAPPER = JsonMapperFactory.create();

    @Override
    public ContributionEvent map(String value) throws Exception {
        return OBJECT_MAPPER.readValue(value, ContributionEvent.class);
    }
}
