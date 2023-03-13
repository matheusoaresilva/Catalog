package com.matheus.catalog.repositories;

import com.matheus.catalog.entities.Role;
import com.matheus.catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
 