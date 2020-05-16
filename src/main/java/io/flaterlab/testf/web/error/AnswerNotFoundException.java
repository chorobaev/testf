package io.flaterlab.testf.web.error;

public final class AnswerNotFoundException extends RuntimeException {

    public AnswerNotFoundException(Long id) {
        super("Answer with id '" + id + "' is not found.");
    }
}
