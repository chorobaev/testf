package io.flaterlab.testf.utils;

import net.minidev.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Json {
    private Map<String, Object> model = new HashMap<>();

    public static Json builder() {
        return new Json();
    }

    public static Map<String, Object> messageSuccess() {
        return Json.builder().put("message", "Success").buildMap();
    }

    private Json() {
    }

    public Json put(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public Map<String, Object> buildMap() {
        return model;
    }

    public String buildJson() {
        return JSONObject.toJSONString(model);
    }

}
