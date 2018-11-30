package com.thtf.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thtf.app.domains.User;


public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByLogin(String username);

	

}