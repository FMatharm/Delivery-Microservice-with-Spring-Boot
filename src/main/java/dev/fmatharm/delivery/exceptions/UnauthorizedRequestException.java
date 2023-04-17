package dev.fmatharm.delivery.exceptions;

public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException(String message) {
        super(message);
    }
}
