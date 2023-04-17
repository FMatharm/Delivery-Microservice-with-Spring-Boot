package dev.fmatharm.delivery.util;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapper {
    public static Map<String, String> map(Errors errors) {
        Map<String, String> errorMap = new HashMap<>();
        errors.getAllErrors().forEach((error) -> {
            errorMap.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return errorMap;
    }
}
