package com.matheus.catalog.repositories;

import com.matheus.catalog.entities.Category;
import com.matheus.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
 