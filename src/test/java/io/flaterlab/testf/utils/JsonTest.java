package io.flaterlab.testf.utils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonTest {

    @Test
    void jsonBuilding_isCorrect() {
        String expected = "{\"username\":\"user\"}";
        String actual = Json.builder().put("username", "user").buildMap();
        assertEquals(expected, actual);
    }
}