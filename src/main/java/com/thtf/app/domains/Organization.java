package com.thtf.app.domains;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Organization extends AbstractAuditingEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@Column
	private String code;

	@ManyToOne
	private Organization parent;

	@OneToMany(mappedBy = "parent")
	private Set<Organization> childen = new HashSet<Organization>();

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public Set<Organization> getChilden() {
		return childen;
	}

	public void setChilden(Set<Organization> childen) {
		this.childen = childen;
	}

}
