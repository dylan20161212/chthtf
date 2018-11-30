package com.thtf.app.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.springframework.security.core.GrantedAuthority;
@Entity
public class MyAuth extends AbstractAuditingEntity implements GrantedAuthority{
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String authPattern;
	
	@Column
	private String resourceList;
	
	@OneToOne
	private UserOrgnizationRole userOrgnizationRole;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthPattern() {
		return authPattern;
	}

	public void setAuthPattern(String authPattern) {
		this.authPattern = authPattern;
	}

	public String getResourceList() {
		return resourceList;
	}

	public void setResourceList(String resourceList) {
		this.resourceList = resourceList;
	}

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.authPattern+"|"+this.resourceList;
	}
	
	
	
	
}
