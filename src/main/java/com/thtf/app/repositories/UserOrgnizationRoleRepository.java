package com.thtf.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thtf.app.domains.UserOrgnizationRole;


public interface UserOrgnizationRoleRepository extends JpaRepository<UserOrgnizationRole, Long> {

	Optional<List<UserOrgnizationRoleRepository>> findByUserId(Long id);

	

}