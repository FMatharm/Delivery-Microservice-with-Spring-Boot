package dev.fmatharm.delivery.util.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class APIResponse {
    private int status;
    public String message;
    public String devMessage;
}
