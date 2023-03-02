package com.matheus.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheus.catalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
 