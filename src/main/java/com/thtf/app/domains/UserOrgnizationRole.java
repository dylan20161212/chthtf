package com.thtf.app.domains;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@Entity
public class UserOrgnizationRole extends AbstractAuditingEntity {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User user;

	@ManyToOne
	private Organization organization;

	@ManyToOne
	private Role role;
	
	@OneToOne(mappedBy="userOrgnizationRole")
	private MyAuth myAuth;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public MyAuth getMyAuth() {
		return myAuth;
	}

	public void setMyAuth(MyAuth myAuth) {
		this.myAuth = myAuth;
	}

	
}
