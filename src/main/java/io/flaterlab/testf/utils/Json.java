package io.flaterlab.testf.utils;

import net.minidev.json.JSONObject;

public class Json {
    private JSONObject jsonObject = new JSONObject();

    public static Json builder() {
        return new Json();
    }

    private Json() {
    }

    public Json put(String key, Object value) {
        jsonObject.put(key, value);
        return this;
    }

    public String build() {
        return jsonObject.toJSONString();
    }
}
