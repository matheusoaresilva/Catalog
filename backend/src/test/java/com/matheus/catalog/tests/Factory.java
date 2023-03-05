package com.matheus.catalog.tests;

import com.matheus.catalog.entities.Category;
import com.matheus.catalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone", 800.0,
                "https://img.com/img.png", Instant.parse("2020-04-20T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletronics"));
        return product;
    }
}