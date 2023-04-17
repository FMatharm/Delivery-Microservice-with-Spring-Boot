package dev.fmatharm.delivery.util.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ObjectResponse {
    private int status;
    private String message;
    private String devMessage;
    private Object ref;
}
