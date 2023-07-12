package com.example.demo.controller;

import com.example.demo.pojo.Category;
import com.example.demo.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DemoController {

    @Autowired
    WebClient.Builder webClient;

    @Autowired
    private ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @GetMapping("/getProduct/{productId}")
    private ResponseEntity<Object> showProduct(@PathVariable Integer productId) {

        ReactiveCircuitBreaker reactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("demo-cb");

        Mono<Object> product = reactiveCircuitBreaker.run(
                webClient.build().get()
                        .uri("http://osa-application/getProductData/" + productId)
                        .retrieve()
                        .bodyToMono(Object.class), throwable -> defaultProductResponse()
        );

        return ResponseEntity.ok(product.block());
    }

    private Mono<Object> defaultProductResponse() {
        Category category = new Category();
        Product product = new Product();
        product.setId(0);
        product.setProductName("Default Product Name");
        product.setManufacturer("Default Product Manufacturer");
        product.setPrice("Default Product Price");
        product.setCategory(category);
        product.setImage(new byte[0]);

        category.setId(0);
        category.setCategoryName("Default Category Name");
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        category.setProduct(productList);
        return Mono.just(product);
    }
}
