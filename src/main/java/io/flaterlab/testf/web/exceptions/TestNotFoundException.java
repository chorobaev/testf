package io.flaterlab.testf.web.exceptions;

public class TestNotFoundException extends RuntimeException {

    public TestNotFoundException(Long id) {
        super("Test: " + id + " is not found.");
    }
}
