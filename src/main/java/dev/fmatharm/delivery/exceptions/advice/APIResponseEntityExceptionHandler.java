package dev.fmatharm.delivery.exceptions.advice;

import dev.fmatharm.delivery.exceptions.BadRequestException;
import dev.fmatharm.delivery.exceptions.ProductNameAlreadyExistsException;
import dev.fmatharm.delivery.exceptions.ResourceNotFoundException;
import dev.fmatharm.delivery.exceptions.UnauthorizedRequestException;
import dev.fmatharm.delivery.util.responses.APIResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class APIResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    public APIResponseEntityExceptionHandler() {
        super();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e, WebRequest request) {
        return handleExceptionInternal(e, message(HttpStatus.NOT_FOUND, e), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ProductNameAlreadyExistsException.class)
    public ResponseEntity<Object> handleProductNameAlreadyExists(ProductNameAlreadyExistsException e, WebRequest request) {
        return handleExceptionInternal(e, message(HttpStatus.CONFLICT, e), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e, WebRequest request) {
        return handleExceptionInternal(e, message(HttpStatus.BAD_REQUEST, e), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedRequestException e, WebRequest request) {
        return handleExceptionInternal(e, message(HttpStatus.UNAUTHORIZED, e), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    private APIResponse message(HttpStatus httpStatus, Exception e) {
        String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
        String devMessage = e.getClass().getSimpleName();

        return new APIResponse(httpStatus.value(), message, devMessage);
    }
}
