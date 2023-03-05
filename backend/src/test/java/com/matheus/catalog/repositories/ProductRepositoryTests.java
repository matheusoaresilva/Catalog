package com.matheus.catalog.repositories;

import com.matheus.catalog.entities.Product;
import com.matheus.catalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }
    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts +1, product.getId());
    }


    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());

    }

    @Test
    public void deleteShouldThrowEmptyResultDataAcessExceptionWhenIdDontExists(){
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);

            Optional<Product> result = repository.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnOptionalDontEmptyWhenIdExists(){

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDontExists(){
        Optional<Product> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }

}
