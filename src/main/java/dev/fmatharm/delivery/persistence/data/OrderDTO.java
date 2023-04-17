package dev.fmatharm.delivery.persistence.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderDTO {
    private long id;
    @NotBlank(message = "address is required")
    private String address;
    @NotBlank(message = "payment is required")
    private String payment;
    private String userId;
    @NotBlank(message = "cartId is required")
    private String cartId;
    private double total;
}
