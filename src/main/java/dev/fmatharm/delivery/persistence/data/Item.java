package dev.fmatharm.delivery.persistence.data;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID", unique = true, nullable = false)
    private long id;
    @Column(name = "ITEM_QUANTITY", nullable = false)
    private int itemQuantity;
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    private Product product;
}
