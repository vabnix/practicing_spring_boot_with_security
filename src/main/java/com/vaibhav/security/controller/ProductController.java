package com.vaibhav.security.controller;

import com.vaibhav.security.model.Product;
import com.vaibhav.security.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public void addProduct(@RequestBody Product product){
        log.info("Adding product: {}", product);
        productService.addProduct(product);
    }

    @GetMapping("/products")
    public List<Product> getAllProduct(){
        log.info("Requesting all products");
        return productService.getAllProducts();
    }
}
