package dev.fmatharm.delivery.web;

import dev.fmatharm.delivery.exceptions.BadRequestException;
import dev.fmatharm.delivery.persistence.data.Cart;
import dev.fmatharm.delivery.persistence.data.CartDTO;
import dev.fmatharm.delivery.persistence.data.Item;
import dev.fmatharm.delivery.persistence.data.Product;
import dev.fmatharm.delivery.service.CartService;
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
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<CartDTO> getAllCarts(@AuthenticationPrincipal Jwt principal) {
        return cartService.getCarts(principal.getSubject());
    }

    @GetMapping("/{id}")
    public List<Item> getCart(@PathVariable String id) {
        try {
            Long cartId = Long.parseLong(id);
            return cartService.getCart(cartId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid cart request");
        }
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Object> createCart(@Valid Product product, BindingResult bindingResult, @AuthenticationPrincipal Jwt principal) {
        if (bindingResult.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(bindingResult));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = cartService.createCart(principal.getSubject(), product);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> createCart(@Valid @RequestBody Product product, Errors errors, @AuthenticationPrincipal Jwt principal) {
        if (errors.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(errors));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = cartService.createCart(principal.getSubject(), product);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(path = "/{id}", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Object> updateCart(@PathVariable String id, @Valid Product product, BindingResult bindingResult, @AuthenticationPrincipal Jwt principal) {
        try {
            Long cartId = Long.parseLong(id);
            if (bindingResult.hasErrors()) {
                ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(bindingResult));
                return ResponseEntity.badRequest().body(response);
            } else {
                return ResponseEntity.ok(cartService.updateCart(cartId, principal.getSubject(), product));
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid cart request");
        }
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> updateCart(@PathVariable String id, @Valid @RequestBody Product product, Errors errors, @AuthenticationPrincipal Jwt principal) {
        try {
            Long cartId = Long.parseLong(id);
            if (errors.hasErrors()) {
                ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(errors));
                return ResponseEntity.badRequest().body(response);
            } else {
                return ResponseEntity.ok(cartService.updateCart(cartId, principal.getSubject(), product));
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid cart request");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCart(@PathVariable String id, @AuthenticationPrincipal Jwt principal) {
        try {
            Long cartId = Long.parseLong(id);
            ObjectResponse response = cartService.deleteCart(cartId, principal.getSubject());
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid cart request");
        }
    }
}
