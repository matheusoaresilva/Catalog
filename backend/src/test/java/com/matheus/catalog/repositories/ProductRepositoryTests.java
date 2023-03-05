package com.matheus.catalog.repositories;

import com.matheus.catalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    ProductRepository repository;
    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){

        Long existingId = 1L;
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        Assertions.assertFalse(result.isPresent());

    }

    @Test
    public void deleteShouldThrowEmptyResultDataAcessExceptionWhenIdDontExists(){
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            Long nonExistingId = 1000L;
            repository.deleteById(nonExistingId);

            Optional<Product> result = repository.findById(nonExistingId);
        });
    }
}
