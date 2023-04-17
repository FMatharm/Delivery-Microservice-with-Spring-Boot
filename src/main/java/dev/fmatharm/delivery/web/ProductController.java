package dev.fmatharm.delivery.web;

import dev.fmatharm.delivery.exceptions.BadRequestException;
import dev.fmatharm.delivery.persistence.data.Product;
import dev.fmatharm.delivery.service.ProductService;
import dev.fmatharm.delivery.util.ErrorMapper;
import dev.fmatharm.delivery.util.responses.ObjectResponse;
import dev.fmatharm.delivery.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> allProducts() {
        return productService.productList();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) {
        try {
            Long productId = Long.parseLong(id);
            return productService.getProduct(productId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid product request");
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Object> saveProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(errors));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = productService.saveProduct(product);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Object> saveProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(bindingResult));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = productService.saveProduct(product);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Object> updateProduct(@Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(errors));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = productService.updateProduct(product);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping(consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Object> updateProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectResponse response = ResponseBuilder.errorResponse(ErrorMapper.map(bindingResult));
            return ResponseEntity.badRequest().body(response);
        } else {
            ObjectResponse response = productService.updateProduct(product);
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable String id) {
        try {
            Long productId = Long.parseLong(id);
            ObjectResponse response = productService.deleteProduct(productId);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid product id");
        }
    }
}
