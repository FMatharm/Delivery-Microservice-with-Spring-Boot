package dev.fmatharm.delivery.util;

import dev.fmatharm.delivery.exceptions.BadRequestException;
import dev.fmatharm.delivery.persistence.data.Cart;
import dev.fmatharm.delivery.persistence.data.Order;
import dev.fmatharm.delivery.persistence.data.OrderDTO;
import dev.fmatharm.delivery.persistence.data.Product;
import dev.fmatharm.delivery.service.OrderService;
import dev.fmatharm.delivery.service.ProductService;
import dev.fmatharm.delivery.util.responses.ObjectResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

public class ResponseBuilder {
    private final ObjectResponse objectResponse;

    private ResponseBuilder(ObjectResponse objectResponse) {
        this.objectResponse = objectResponse;
    }

    public static ResponseBuilder productResponse(Product product) {
        ObjectResponse newObjectResponse = new ObjectResponse(HttpStatus.OK.value(), "", ProductService.class.getSimpleName(), product);
        return new ResponseBuilder(newObjectResponse);
    }

    public static ObjectResponse cartResponse(Cart cart) {
        return new ObjectResponse(HttpStatus.OK.value(), "Cart created with item " + cart.getItemList().get(0).getId(), "CART ID " + cart.getId(), cart.getItemList().get(0));
    }

    public static ResponseBuilder deletedResource(Object o) {
        ObjectResponse newObjectResponse = new ObjectResponse(HttpStatus.OK.value(), "", Service.class.getSimpleName(), o);
        return new ResponseBuilder(newObjectResponse);
    }

    public static ResponseBuilder orderResponse(OrderDTO order) {
        ObjectResponse newObjectResponse = new ObjectResponse(HttpStatus.OK.value(), "", OrderService.class.getSimpleName(), order);
        return new ResponseBuilder(newObjectResponse);
    }

    public static ObjectResponse errorResponse(Map<String, String> errorMap) {
        return new ObjectResponse(HttpStatus.BAD_REQUEST.value(), "Invalid fields", BadRequestException.class.getSimpleName(), errorMap);
    }

    public ObjectResponse message(String message) {
        this.objectResponse.setMessage(message);
        return objectResponse;
    }
}
