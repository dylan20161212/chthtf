package com.thtf.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thtf.app.domains.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

	

}