package com.thtf.code.gen.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CodeEntity {
	private String basePackage = "com.thtf.app";
	private String importLib =  "import java.io.Serializable;\n"+
								"import java.time.ZonedDateTime;\n\n"+
								
								"import javax.persistence.*;\n"+
								"import javax.validation.constraints.*;\n"+
								"import org.hibernate.validator.constraints.*;\n"+
								"import javax.persistence.Entity;\n"+
								"import org.hibernate.annotations.Table;\n"+
								"import org.hibernate.annotations.Cache;\n"+
//								"import javax.persistence.Entity;\n"+
//								"import javax.persistence.GeneratedValue;\n"+
//								"import javax.persistence.GenerationType;\n"+
//								"import javax.persistence.Id;\n"+
//								"import javax.persistence.Table;\n\n"+
								
//								"import org.hibernate.annotations.Cache;\n"+
//								"import org.hibernate.annotations.CacheConcurrencyStrategy;\n"+
								"import org.hibernate.annotations.*;\n";
	private String comment;
	private String name;
	private String tableName;
	
	
	
	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getImportLib() {
		return importLib;
	}

	public void setImportLib(String importLib) {
		this.importLib = importLib;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	private List<CodeRelation> relations = new ArrayList<CodeRelation>();
	private List<CodeField> fields = new ArrayList<CodeField>();
	
	public CodeEntity(Map<String,Object> entity ,List<Map<String,Object>> relations){
		this.name = (String) entity.get("name");
		this.tableName = (String) entity.get("tableName");
		Optional<String> tableName = Optional.ofNullable(this.tableName);

        if(tableName.isPresent()){
        	this.tableName = this.tableName.toLowerCase();
        }else{
        	this.tableName = this.name.toLowerCase();
        }
	
		this.comment = (String) entity.get("comment");
		if(this.comment == null || this.comment.isEmpty()){
			this.comment = this.tableName;
		}
		List<Map<String,Object>> fields = (List<Map<String, Object>>) Optional.ofNullable(entity.get("fields")).orElse(new ArrayList<Map<String,Object>>());
		
		this.fields = (List<CodeField>) fields.stream().map((field)->{
			return new CodeField(field,this);
		}).collect(Collectors.toList());
		
		this.relations = relations.stream().map((relation)->{
			CodeRelation cr = new CodeRelation();
			cr.setName((String) relation.get("key"));
			Map<String,String> from = (Map<String, String>) relation.get("from");
			cr.setFromName(from.get("relationName"));
			cr.setFromEntity(from.get("entity"));
			cr.setFromDisplay(from.get("display"));
			
			Map<String,String> to = (Map<String, String>) relation.get("to");
			cr.setToName(to.get("relationName"));
			cr.setToEntity(to.get("entity"));
			cr.setToDisplay(to.get("display"));
			
			cr.setCodeEntity(this);
			return cr;
		}).collect(Collectors.toList());
	}
	
	
	public String generateDomainCode(){
		String classx = 
				"package "+this.basePackage+".domains; \n [importLib] \n"+
				
				"/**\n"+
				 "* \n"+this.comment+
				 "*/\n"+
				 "/**\n"+
				 "* @author Administrator\n"+
				 "*\n"+
				 "*/\n"+
				//"@ApiModel(description = \""+this.comment+"\")\n"+
				"@Entity(name =\""+this.tableName+"\")\n"+
				"@Table(appliesTo = \""+this.tableName+"\",comment=\""+this.comment+"\") \n"+
				"@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)\n"+
				"public class "+this.name+" extends AbstractAuditingEntity implements Serializable { \n\n"+
				"   private static final long serialVersionUID = 1L;\n\n\n"+

			    "  @Id \n"+
			    "  @GeneratedValue(strategy = GenerationType.IDENTITY)\n"+
			    "  private Long id;\n";
		
		     
		     List<String> fieldCodes = (List<String>) this.fields.stream().map((field)->{
		    	 
		    	 return field.generateDomainCodeField();
		    	 
		     }).collect(Collectors.toList());
		     List<String> getSetCodes = (List<String>) this.fields.stream().map((field)->{
		    	 
		    	 return field.generateDomainCodeGetSet();
		    	 
		     }).collect(Collectors.toList());
		     String relationCode = "";
		     
		     for (CodeRelation relation : this.relations) {
		    	 relationCode+=relation.generateDomainCode()+"\n";
			}
		     String codesx = "";
		     for (String fieldCode : fieldCodes) {
		    	 codesx+=fieldCode;
			 }
		     for (String getSetCode : getSetCodes) {
		    	 codesx+=getSetCode;
			 }
			 String finalCode = classx+"\n"+codesx+"\n"+relationCode+"\n }";
		return (this.importLib != null)?finalCode.replace("[importLib]", this.importLib):finalCode.replace("[importLib]", " ");
	}
	
	
	public String generateRepositoryCode(){
		//TODO
		return null;
	}
	
	
	public String generateServiceCode(){
		//TODO
		return null;
	}
	
	public String generateResourceCode(){
		//TODO
		return null;
	}
	
	
	public String generateMapStructCode(){
		//TODO
		return null;
	}

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
