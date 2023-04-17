package dev.fmatharm.delivery.persistence.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CartDTO {
    private long id;
    private String userId;
    private long orderId;
    private List<Item> items;
}
