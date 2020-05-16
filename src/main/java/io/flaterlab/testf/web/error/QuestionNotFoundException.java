package io.flaterlab.testf.web.error;

public final class QuestionNotFoundException extends RuntimeException {

    public QuestionNotFoundException(Long id) {
        super("Question with id '" + id + "' is not found.");
    }
}
