package dev.fmatharm.delivery.service;

import dev.fmatharm.delivery.exceptions.BadRequestException;
import dev.fmatharm.delivery.exceptions.ResourceNotFoundException;
import dev.fmatharm.delivery.exceptions.UnauthorizedRequestException;
import dev.fmatharm.delivery.persistence.data.Cart;
import dev.fmatharm.delivery.persistence.data.CartDTO;
import dev.fmatharm.delivery.persistence.data.Item;
import dev.fmatharm.delivery.persistence.data.Product;
import dev.fmatharm.delivery.persistence.repo.ItemRepository;
import dev.fmatharm.delivery.persistence.repo.CartRepository;
import dev.fmatharm.delivery.persistence.repo.ProductRepository;
import dev.fmatharm.delivery.util.ItemMapper;
import dev.fmatharm.delivery.util.ResponseBuilder;
import dev.fmatharm.delivery.util.responses.ObjectResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ItemRepository itemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
    }

    public List<CartDTO> getCarts(String userId) {
        return cartListToDto(cartRepository.findByUserId(userId));
    }

    public List<Item> getCart(Long id) throws ResourceNotFoundException {
        Optional<Cart> found = cartRepository.findById(id);
        if (found.isEmpty()) {
            throw new ResourceNotFoundException("Cart not found");
        }
        return found.get().getItemList();
    }

    public ObjectResponse createCart(String userId, Product product) throws RuntimeException {
        if (product.getStockQuantity() <= 0) {
            throw new BadRequestException("Item quantity must be greater than 0");
        }

        Product foundProduct = productRepository.findByProductName(product.getProductName());

        if (foundProduct == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        Item item = ItemMapper.toItemWithQuantity(foundProduct, product.getStockQuantity());
        foundProduct.setStockQuantity(foundProduct.getStockQuantity() - item.getItemQuantity());
        Item savedItem = itemRepository.save(item);

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItemList(new ArrayList<>());
        cart.getItemList().add(savedItem);

        cartRepository.save(cart);

        return ResponseBuilder.cartResponse(cart);
    }

    public List<Item> updateCart(Long cartId, String userId, Product product) throws RuntimeException {
        Optional<Cart> foundCart = cartRepository.findById(cartId);
        if (foundCart.isEmpty()) {
            throw new ResourceNotFoundException("Cart not found");
        } else if (!foundCart.get().getUserId().equals(userId)) {
            throw new UnauthorizedRequestException("Cart does not belong to user");
        } else if (foundCart.get().getOrder() != null) {
            throw new UnauthorizedRequestException("Can't update an already ordered cart");
        }

        Product foundProduct = productRepository.findByProductName(product.getProductName());

        if (foundProduct == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        int index = itemInCart(foundCart.get(), foundProduct.getProductName());

        if (index != -1) {
            Item item = foundCart.get().getItemList().get(index);
            item.setItemQuantity(item.getItemQuantity() + product.getStockQuantity());

            if (item.getItemQuantity() <= 0) {
                foundCart.get().getItemList().remove(item);
                itemRepository.delete(item);
                if (foundCart.get().getItemList().isEmpty()) {
                    cartRepository.delete(foundCart.get());
                }
            } else {
                itemRepository.save(item);
            }

            foundProduct.setStockQuantity(foundProduct.getStockQuantity() - product.getStockQuantity());
            productRepository.save(foundProduct);
        } else {
            if (product.getStockQuantity() <= 0) {
                throw new BadRequestException("Item quantity must be greater than 0");
            }
            Item item = ItemMapper.toItemWithQuantity(foundProduct, product.getStockQuantity());
            foundProduct.setStockQuantity(foundProduct.getStockQuantity() - product.getStockQuantity());
            Item savedItem = itemRepository.save(item);

            foundCart.get().getItemList().add(savedItem);
            cartRepository.save(foundCart.get());
        }

        return foundCart.get().getItemList();
    }

    public ObjectResponse deleteCart(Long cartId, String userId) {
        Optional<Cart> foundCart = cartRepository.findById(cartId);

        if (foundCart.isEmpty()) {
            throw new ResourceNotFoundException("Cart not found");
        } else if (!foundCart.get().getUserId().equals(userId)) {
            throw new UnauthorizedRequestException("Cart does not belong to user");
        } else if (foundCart.get().getOrder() != null) {
            throw new UnauthorizedRequestException("Can't delete an already ordered cart");
        }

        cartRepository.delete(foundCart.get());
        return ResponseBuilder.deletedResource(foundCart.get().getId()).message("Cart deleted");
    }

    private int itemInCart(Cart cart, String itemName) {
        for (int i = 0; i < cart.getItemList().size(); i++) {
            Item item = cart.getItemList().get(i);
            if (item.getProduct().getProductName().equals(itemName))
                return i;
        }
        return -1;
    }

    private CartDTO cartToDto(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setOrderId(cart.getOrder() != null ? cart.getOrder().getOrderId() : 0);
        dto.setUserId(cart.getUserId());
        dto.setItems(cart.getItemList());
        return dto;
    }

    private List<CartDTO> cartListToDto(List<Cart> cartList) {
        List<CartDTO> dtoList = new ArrayList<>();
        for (Cart cart : cartList) {
            dtoList.add(cartToDto(cart));
        }
        return dtoList;
    }
}
