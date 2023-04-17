package dev.fmatharm.delivery.persistence.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID", unique = true)
    private long orderId;
    @NotBlank(message = "An address for shipping is required")
    @Column(name = "ADDRESS", nullable = false)
    private String address;
    @NotBlank(message = "Payment is required")
    @Column(name = "PAYMENT", nullable = false)
    private String payment;
    @Column(name = "USER_ID", nullable = false)
    private String userId;
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CART_ID", referencedColumnName = "CART_ID")
    private Cart cart;
}
