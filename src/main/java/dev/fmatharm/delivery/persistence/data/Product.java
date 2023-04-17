package dev.fmatharm.delivery.persistence.data;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "INVENTORY")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID", unique = true)
    private long productId;
    @NotBlank(message = "Product name is required")
    @Column(name = "PRODUCT_NAME", unique = true, nullable = false)
    private String productName;
    @Column(name = "PRODUCT_STOCK")
    private int stockQuantity;
    @Column(name = "PRODUCT_PRICE", nullable = false)
    private double productPrice;
}
