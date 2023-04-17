package dev.fmatharm.delivery.util;

import dev.fmatharm.delivery.persistence.data.Item;
import dev.fmatharm.delivery.persistence.data.Product;

public class ItemMapper {
    public static Item toItemWithQuantity(Product product, int quantity) {
        Item item = new Item();
        item.setProduct(product);
        item.setItemQuantity(quantity);
        return item;
    }
}
