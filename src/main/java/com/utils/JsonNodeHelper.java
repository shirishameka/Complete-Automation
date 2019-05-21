package com.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class JsonNodeHelper {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode stringToJsonNode(String str) throws IOException {
        return objectMapper.readTree(str);
    }

    public static String jsonNodeToString(JsonNode node) throws IOException {
        return objectMapper.writeValueAsString(node);
    }

    public static JsonNode editJsonNode(JsonNode node, String key, String value) {
        ((ObjectNode) node).put(key, value);
        return node;
    }

    public static JsonNode editJsonNode(JsonNode node, String key, Object value) {
        ((ObjectNode) node).putPOJO(key, value);
        return node;
    }

    public static String getValueFromJsonNode(JsonNode node, String key) {
        return node.path(key).asText();
    }

    public static JsonNode getInnerJsonNode(String json, String key) throws IOException {
        return objectMapper.readTree(json).get(key);
    }

    public static JsonNode convertObjectToJsonNode(Object object) {
        JsonNode jsonAdObject = objectMapper.valueToTree(object);
        return jsonAdObject;
    }

    /***
     * returns matching Json Object from JsonArray
     * @param jsonArray
     * @param key
     * @param value
     * @return matching JSON Object
     *
     */

    public static JsonNode getJsonObjectFromJsonArray(JSONArray jsonArray, String key, Object value) throws Exception {
        String matchedObject = "";

        for (Object object : jsonArray) {
            JSONObject jsonObject = new JSONObject(object.toString());
            if (jsonObject.get(key) == value || jsonObject.get(key).toString().equals(value)) {
                matchedObject = jsonObject.toString();
                break;
            }
        }
        return stringToJsonNode(matchedObject);
    }

    public static JSONArray stringToJsonArray(String str) throws IOException {
        return new JSONArray(str);
    }

}