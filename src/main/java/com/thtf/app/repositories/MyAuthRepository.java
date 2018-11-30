package com.thtf.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thtf.app.domains.MyAuth;


public interface MyAuthRepository extends JpaRepository<MyAuth, Long> {

	

}