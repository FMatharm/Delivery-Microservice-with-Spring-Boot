package dev.fmatharm.delivery.exceptions;

public class ProductNameAlreadyExistsException extends RuntimeException {
    public ProductNameAlreadyExistsException(String message) {
        super(message);
    }
}
