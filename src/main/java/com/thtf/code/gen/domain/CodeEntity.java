package com.thtf.code.gen.domain;

import java.util.ArrayList;
import java.util.List;

public class CodeEntity {
	private String name;
	private String tableName;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	private List<CodeRelation> relations = new ArrayList<CodeRelation>();
	private List<CodeField> fields = new ArrayList<CodeField>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CodeRelation> getRelations() {
		return relations;
	}

	public void setRelations(List<CodeRelation> relations) {
		this.relations = relations;
	}

	public List<CodeField> getFields() {
		return fields;
	}

	public void setFields(List<CodeField> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "CodeEntity [name=" + name + ", tableName=" + tableName + "]";
	}
	
	
	

}
