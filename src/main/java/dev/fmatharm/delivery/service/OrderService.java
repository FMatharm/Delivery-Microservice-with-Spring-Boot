package dev.fmatharm.delivery.service;

import dev.fmatharm.delivery.exceptions.BadRequestException;
import dev.fmatharm.delivery.exceptions.ResourceNotFoundException;
import dev.fmatharm.delivery.exceptions.UnauthorizedRequestException;
import dev.fmatharm.delivery.persistence.data.Cart;
import dev.fmatharm.delivery.persistence.data.Item;
import dev.fmatharm.delivery.persistence.data.Order;
import dev.fmatharm.delivery.persistence.data.OrderDTO;
import dev.fmatharm.delivery.persistence.repo.CartRepository;
import dev.fmatharm.delivery.persistence.repo.OrderRepository;
import dev.fmatharm.delivery.util.ResponseBuilder;
import dev.fmatharm.delivery.util.responses.ObjectResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    public List<OrderDTO> getOrders(String userId) {
        return orderListToDTO(orderRepository.findByUserId(userId));
    }

    public OrderDTO getOrder(Long id) {
        Optional<Order> found = orderRepository.findById(id);
        if (found.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        }
        return orderToDTO(found.get());
    }

    public ObjectResponse createOrder(String userId, OrderDTO orderDTO) {
        try {
            Long cartId = Long.parseLong(orderDTO.getCartId());
            Optional<Cart> foundCart = cartRepository.findById(cartId);

            if (foundCart.isEmpty()) {
                throw new ResourceNotFoundException("Cart not found");
            } else if (foundCart.get().getOrder() != null) {
                throw new BadRequestException("Cart is already ordered");
            }

            Order order = new Order();
            order.setUserId(userId);
            order.setPayment(orderDTO.getPayment());
            order.setAddress(orderDTO.getAddress());
            order.setCart(foundCart.get());

            Order saved = orderRepository.save(order);
            foundCart.get().setOrder(saved);

            return ResponseBuilder.orderResponse(orderToDTO(saved)).message("Order created successfully");
        } catch (NumberFormatException e) {
            throw  new BadRequestException("Invalid cart ID");
        }
    }

    public ObjectResponse deleteOrder(String userId, Long orderId) {
        Optional<Order> foundOrder = orderRepository.findById(orderId);
        if (foundOrder.isEmpty()) {
            throw new ResourceNotFoundException("Order not found");
        } else if (!foundOrder.get().getUserId().equals(userId)) {
            throw new UnauthorizedRequestException("Order does not belong to user");
        }

        orderRepository.delete(foundOrder.get());
        return ResponseBuilder.deletedResource(orderToDTO(foundOrder.get())).message("Order deleted successfully");
    }

    private OrderDTO orderToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getOrderId());
        dto.setAddress(order.getAddress());
        dto.setPayment(order.getPayment());
        dto.setCartId(String.valueOf(order.getCart().getId()));
        dto.setUserId(order.getUserId());

        double sub = 0;

        for (Item item : order.getCart().getItemList()) {
            sub += item.getProduct().getProductPrice() * item.getItemQuantity();
        }

        dto.setTotal(sub);
        return dto;
    }

    private List<OrderDTO> orderListToDTO(List<Order> orderList) {
        List<OrderDTO> dtoList = new ArrayList<>();
        for (Order order : orderList) {
            dtoList.add(orderToDTO(order));
        }
        return dtoList;
    }
}
