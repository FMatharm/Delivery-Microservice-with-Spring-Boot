package dev.fmatharm.delivery.persistence.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CARTS")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID", unique = true)
    private long id;
    @Column(name = "USER_ID", nullable = false)
    private String userId;
    @ToString.Exclude
    @OneToOne(mappedBy = "cart")
    private Order order;
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "CART_ITEMS",
            joinColumns = @JoinColumn(name = "CART_ID", referencedColumnName = "CART_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
    )
    private List<Item> itemList;
}
