package com.diachenko.dev_pulse_jira_service.service;
/*  Dev_Pulse
    15.05.2025
    @author DiachenkoDanylo
*/

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {
    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    .toFormatter();

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String date = p.getText();
        return ZonedDateTime.parse(date, FORMATTER).toInstant();
    }
}
