package com.matheus.catalog.services;

import com.matheus.catalog.dto.ProductDTO;
import com.matheus.catalog.entities.Category;
import com.matheus.catalog.entities.Product;
import com.matheus.catalog.repositories.CategoryRepository;
import com.matheus.catalog.repositories.ProductRepository;
import com.matheus.catalog.services.exceptions.DatabaseException;
import com.matheus.catalog.services.exceptions.ResourceNotFoundException;
import com.matheus.catalog.tests.Factory;
import org.hibernate.action.internal.EntityActionVetoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    ProductService service;

    @Mock
    ProductRepository repository;

    @Mock
    CategoryRepository categoryRepository;

    Long existingId;
    Long nonExistingId;
    Long dependentId;
    PageImpl<Product> page;
    Product product;
    Category category;


    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        dependentId = 4L;
        product = Factory.createProduct();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(product));

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);

        Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityActionVetoException.class);

        Mockito.when(repository.getOne(existingId)).thenReturn(product);

        Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityActionVetoException.class);

        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));

        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.doNothing().when(repository).deleteById(existingId);

        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists(){

        Assertions.assertThrows(ResourceNotFoundException.class,() ->{
            service.findById(nonExistingId);
        });

    }
    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists(){
        ProductDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);

    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId(){

        Assertions.assertThrows(DatabaseException.class,() ->{
            service.delete(dependentId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }
    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class,() ->{
            service.delete(nonExistingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }
    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() ->{
            service.delete(existingId);
        });

        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }
}
