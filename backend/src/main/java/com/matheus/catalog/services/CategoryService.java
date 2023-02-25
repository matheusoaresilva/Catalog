package com.matheus.catalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matheus.catalog.entities.Category;
import com.matheus.catalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<Category> findAll() { 
		return repository.findAll();
	}
}
