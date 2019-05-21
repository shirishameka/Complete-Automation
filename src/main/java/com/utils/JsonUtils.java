package com.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


public class JsonUtils {
    public final static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static String convertMapToJson(Map<String, Object> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    public static JsonNode readJsonStringFromFile(String path) throws IOException {
        JsonNode node = getObjectMapper().readTree(new File(path));
        return node;
    }

    public static JsonNode getJsonFromString(String jsonString) throws IOException {
        JsonNode node = getObjectMapper().readTree(jsonString);
        return node;
    }

    public static void writeJsonToFile(String json, String filePath) {
        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);

            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject));
            fileWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> Map<String, T> getMapFromJson(String json) throws Exception {
        Map<String, T> value = getObjectMapper().readValue(json, new TypeReference<HashMap<String, T>>() {
        });
        return value;
    }

    public static String getJsonStringFromObject(Object obj) throws JsonGenerationException,
            JsonMappingException, IOException {
        StringWriter writer = new StringWriter();
        getObjectMapper().writeValue(writer, obj);
        return writer.toString();
    }


    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}

