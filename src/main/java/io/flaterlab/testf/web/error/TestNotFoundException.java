package io.flaterlab.testf.web.error;

public final class TestNotFoundException extends RuntimeException {

    public TestNotFoundException(Long id) {
        super("Test with id: '" + id + "' is not found.");
    }
}
