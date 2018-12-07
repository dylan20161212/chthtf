package com.thtf.code.gen.domain;

import java.util.HashMap;
import java.util.Map;

public class CodeRelation {
	private Map<String, String> manayToOne = new HashMap<String, String>();
	private Map<String, String> oneToMany = new HashMap<String, String>();
	private Map<String, String> oneToOne = new HashMap<String, String>();
	private Map<String, String> manyToMany = new HashMap<String, String>();

	public Map<String, String> getManayToOne() {
		return manayToOne;
	}

	public void setManayToOne(Map<String, String> manayToOne) {
		this.manayToOne = manayToOne;
	}

	public Map<String, String> getOneToMany() {
		return oneToMany;
	}

	public void setOneToMany(Map<String, String> oneToMany) {
		this.oneToMany = oneToMany;
	}

	public Map<String, String> getOneToOne() {
		return oneToOne;
	}

	public void setOneToOne(Map<String, String> oneToOne) {
		this.oneToOne = oneToOne;
	}

	public Map<String, String> getManyToMany() {
		return manyToMany;
	}

	public void setManyToMany(Map<String, String> manyToMany) {
		this.manyToMany = manyToMany;
	}

}
