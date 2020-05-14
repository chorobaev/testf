package io.flaterlab.testf.web.error;

public final class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287162L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final Throwable cause) {
        super(cause);
    }

}
