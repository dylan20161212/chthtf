package com.thtf.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thtf.app.domains.Organization;


public interface OrganizationRepository extends JpaRepository<Organization, Long> {

	

}