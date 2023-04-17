package dev.fmatharm.delivery.service;

import dev.fmatharm.delivery.exceptions.ProductNameAlreadyExistsException;
import dev.fmatharm.delivery.exceptions.ResourceNotFoundException;
import dev.fmatharm.delivery.persistence.data.Product;
import dev.fmatharm.delivery.persistence.repo.ProductRepository;
import dev.fmatharm.delivery.util.responses.ObjectResponse;
import dev.fmatharm.delivery.util.ResponseBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> productList() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) throws ResourceNotFoundException {
        Optional<Product> found = productRepository.findById(id);
        if (found.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return found.get();
    }

    public ObjectResponse saveProduct(Product product) throws ProductNameAlreadyExistsException {
        Product foundByName = productRepository.findByProductName(product.getProductName());
        if (foundByName != null) {
            throw new ProductNameAlreadyExistsException("Name already exists");
        }
        productRepository.save(product);
        return ResponseBuilder.productResponse(product).message("Product saved");
    }

    public ObjectResponse updateProduct(Product product) throws ResourceNotFoundException {
        Product found = productRepository.findByProductName(product.getProductName());
        if (found != null) {
            product.setProductId(found.getProductId());
            productRepository.save(product);
            return ResponseBuilder.productResponse(product).message("Product updated");
        } else {
            throw new ResourceNotFoundException("Product not found");
        }
    }

    public ObjectResponse deleteProduct(Long id) throws ResourceNotFoundException {
        Optional<Product> foundProduct = productRepository.findById(id);
        if (foundProduct.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.delete(foundProduct.get());
        return ResponseBuilder.productResponse(foundProduct.get()).message("Product deleted");
    }
}
