package dev.fmatharm.delivery.web;

import dev.fmatharm.delivery.exceptions.BadRequestException;
import dev.fmatharm.delivery.persistence.data.Order;
import dev.fmatharm.delivery.persistence.data.OrderDTO;
import dev.fmatharm.delivery.service.OrderService;
import dev.fmatharm.delivery.util.ErrorMapper;
import dev.fmatharm.delivery.util.ResponseBuilder;
import dev.fmatharm.delivery.util.responses.ObjectResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDTO> allOrders(@AuthenticationPrincipal Jwt principal) {
        return orderService.getOrders(principal.getSubject());
    }

    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable String id) {
        try {
            Long orderId = Long.parseLong(id);
            return orderService.getOrder(orderId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid order request");
        }
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Object> createOrder(@Valid OrderDTO order, BindingResult bindingResult, @AuthenticationPrincipal Jwt principal) {
        if (bindingResult.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(bindingResult));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = orderService.createOrder(principal.getSubject(), order);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderDTO order, Errors errors, @AuthenticationPrincipal Jwt principal) {
        if (errors.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(errors));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = orderService.createOrder(principal.getSubject(), order);
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable String id, @AuthenticationPrincipal Jwt principal) {
        try {
            Long orderId = Long.parseLong(id);
            ObjectResponse response = orderService.deleteOrder(principal.getSubject(), orderId);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid order ID");
        }
    }
}
