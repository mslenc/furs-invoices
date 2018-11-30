package com.github.mslenc.furslib.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JSON {
    private JSON() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new KotlinModule());
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public static String stringify(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    public static void writeJson(Object value, OutputStream out) throws IOException {
        objectMapper.writeValue(out, value);
    }

    public static <T> T parse(String value, Class<T> klass) throws IOException {
        return objectMapper.readValue(value, klass);
    }

    public static <T> T parse(InputStream input, Class<T> klass) throws IOException {
        return objectMapper.readValue(input, klass);
    }
}
