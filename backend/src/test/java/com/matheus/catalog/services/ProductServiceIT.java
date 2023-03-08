package com.matheus.catalog.services;

import com.matheus.catalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIT {

    @Autowired
    ProductService service;

    @Autowired
    ProductRepository repository;

    Long existingId;
    Long nonExistingId;
    Long countTotalProducts;

}
