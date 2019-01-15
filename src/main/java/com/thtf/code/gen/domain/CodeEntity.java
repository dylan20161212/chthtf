package com.thtf.code.gen.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.thtf.code.gen.util.AppenderUtil;
import com.thtf.code.gen.util.FileUtil;

public class CodeEntity {
	private String basePackage = "com.thtf.app";
	private String templatePath;
	private String importLib = "import java.io.Serializable;\n" + "import java.time.ZonedDateTime;\n\n" +

			"import javax.persistence.*;\n" + "import javax.validation.constraints.*;\n"
			+ "import org.hibernate.validator.constraints.*;\n" + "import javax.persistence.Entity;\n"
			+ "import org.hibernate.annotations.Table;\n" + "import org.hibernate.annotations.Cache;\n" +
			// "import javax.persistence.Entity;\n"+
			// "import javax.persistence.GeneratedValue;\n"+
			// "import javax.persistence.GenerationType;\n"+
			// "import javax.persistence.Id;\n"+
			// "import javax.persistence.Table;\n\n"+

			// "import org.hibernate.annotations.Cache;\n"+
			// "import org.hibernate.annotations.CacheConcurrencyStrategy;\n"+
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
	private List<CodeRelation> allRelations = new ArrayList<CodeRelation>();
	
	private List<CodeField> fields = new ArrayList<CodeField>();

	public CodeEntity(String templatePath,String basePackage,Map<String, Object> entity, List<Map<String, Object>> relations,List<Map<String, Object>> allRelations) {
		this.templatePath = templatePath;
		this.basePackage = basePackage;
		this.name = (String) entity.get("name");
		this.tableName = (String) entity.get("tableName");
		Optional<String> tableName = Optional.ofNullable(this.tableName);

		if (tableName.isPresent()) {
			this.tableName = this.tableName.toLowerCase();
		} else {
			this.tableName = this.name.toLowerCase();
		}

		this.comment = (String) entity.get("comment");
		if (this.comment == null || this.comment.isEmpty()) {
			this.comment = this.tableName;
		}
		List<Map<String, Object>> fields = (List<Map<String, Object>>) Optional.ofNullable(entity.get("fields"))
				.orElse(new ArrayList<Map<String, Object>>());

		this.fields = (List<CodeField>) fields.stream().map((field) -> {
			return new CodeField(field, this);
		}).collect(Collectors.toList());

		this.relations = genCodeRelations(relations);
		this.allRelations = genCodeRelations(allRelations);
	}

	/**
	 * @param relations
	 */
	private List<CodeRelation> genCodeRelations(List<Map<String, Object>> relations) {
		return relations.stream().map((relation) -> {
			CodeRelation cr = new CodeRelation();
			cr.setName((String) relation.get("key"));
			Map<String, String> from = (Map<String, String>) relation.get("from");
			cr.setFromName(from.get("relationName"));
			cr.setFromEntity(from.get("entity"));
			cr.setFromDisplay(from.get("display"));

			Map<String, String> to = (Map<String, String>) relation.get("to");
			cr.setToName(to.get("relationName"));
			cr.setToEntity(to.get("entity"));
			cr.setToDisplay(to.get("display"));

			cr.setCodeEntity(this);
			return cr;
		}).collect(Collectors.toList());
	}

	public String generateDomainCode() {
		String classx = "package " + this.basePackage + ".domain; \n [importLib] \n" +

				"/**\n" + "* \n" + this.comment + "*/\n" + "/**\n" + "* @author Administrator\n" + "*\n" + "*/\n" +
				// "@ApiModel(description = \""+this.comment+"\")\n"+
				"@Entity(name =\"" + this.tableName + "\")\n" + "@Table(appliesTo = \"" + this.tableName
				+ "\",comment=\"" + this.comment + "\") \n"
				+ "@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)\n" + "public class " + this.name
				+ " extends AbstractAuditingEntity implements Serializable { \n\n"
				+ "   private static final long serialVersionUID = 1L;\n\n\n" +

				"  @Id \n" + "  @GeneratedValue(strategy = GenerationType.IDENTITY)\n" + "  private Long id;\n";

		List<String> fieldCodes = (List<String>) this.fields.stream().map((field) -> {

			return field.generateDomainCodeField();

		}).collect(Collectors.toList());
		List<String> getSetCodes = (List<String>) this.fields.stream().map((field) -> {

			return field.generateDomainCodeGetSet();

		}).collect(Collectors.toList());
		String relationCode = "";
		String relationGetSetCode = "";

		for (CodeRelation relation : this.relations) {
			relationCode += relation.generateDomainCode() + "\n";
			relationGetSetCode+= relation.generateDomainGetSetCode() + "\n";
		}
		String codesx = "";
		for (String fieldCode : fieldCodes) {
			codesx += fieldCode;
		}
		codesx += "public Long getId(){\n return this.id;\n}\n";
		codesx += "public void setId(Long id){\n this.id=id;\n}\n";
		for (String getSetCode : getSetCodes) {
			codesx += getSetCode;
		}
		String finalCode = classx + "\n" + codesx+relationGetSetCode + "\n" + relationCode + "\n }";
		return (this.importLib != null) ? finalCode.replace("[importLib]", this.importLib)
				: finalCode.replace("[importLib]", " ");
	}

	public String generateLiquibase(String way) {
		final String toWay = Optional.ofNullable(way).orElse("xml");
		String code = "";
		if (toWay.equals("xml")) {
			code += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +

					"<databaseChangeLog" + "        xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\""
					+ "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
					+ "    xmlns:ext=\"http://www.liquibase.org/xml/ns/dbchangelog-ext\""
					+ "    xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.1.xsd"
					+ "    http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd\">";
			code += " <changeSet id=\"init_" + this.tableName + "\" author=\"liuyang\" >"
					+ "	   <comment>init schema</comment>" + "	   <createTable tableName=\"" + this.tableName
					+ "\" remarks=\"" + this.comment + "\">";
			List<String> fieldstrs = this.fields.stream().map((field) -> {
				return field.generateLiquibase(toWay);
			}).collect(Collectors.toList());
			String fieldCodeStr = "\n <column name=\"id\" type=\"bigint\" autoIncrement=\"${autoIncrement}\">"+
									"\n       <constraints primaryKey=\"true\" nullable=\"false\"/>"+
									"\n </column>\n";
			for (String fieldstr : fieldstrs) {
				fieldCodeStr += fieldstr + "\n";
			}
			
			code += fieldCodeStr;

		}
		
		code+=" </createTable> \n\t</changeSet> \n </xml>";
		
		return code;
		

	}
	
	public String generateDBScriptCode(){
		AppenderUtil appender = AppenderUtil.newOne();
		String code = "create table `"+this.tableName+"` (";
		appender.append(code);
		appender.append(" `id` bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,");
		for (CodeField codeField : fields) {
			appender.append(codeField.generateDBScriptCode());
		}
		appender.deleteLast(",");
		String mycomment = "";
		if(this.comment!=null && !this.comment.trim().equals("")){
			mycomment = "comment '"+this.comment+"'";
		}
		appender.append(" ) "+mycomment+" ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		return appender.toString();
	}
	

	
	

	public String generateRepositoryCode() {
		  
		AppenderUtil appender = AppenderUtil.newOne();
		appender.append("package "+this.basePackage+".repository;");
		appender.append("import "+this.basePackage+".domain."+this.name+";");
		appender.append("import org.springframework.stereotype.Repository;");
		appender.append("import org.springframework.data.jpa.repository.*;");
		appender.append("@SuppressWarnings(\"unused\")");
		appender.append("@Repository");
		appender.append("public interface "+this.name+"Repository extends BaseRepository<"+this.name+">{");
		appender.append("  ");
		appender.append("}");
	    
		return appender.toString();
		
	}

	public String generateServiceCode() {
		String templatePath = this.templatePath+"/Service.tpl";
		String content = FileUtil.read(templatePath);
		content = content.replaceAll("\\[basePackage\\]", this.basePackage);
		content = content.replaceAll("\\[domainName\\]", this.name);
		content = content.replaceAll("\\[domainNameVar\\]", this.firstLowCase(this.name));
		return content;
	}

	public String generateResourceCode() {
		String templatePath = this.templatePath+"/Resource.tpl";
		String content = FileUtil.read(templatePath);
		content = content.replaceAll("\\[basePackage\\]", this.basePackage);
		content = content.replaceAll("\\[domainName\\]", this.name);
		content = content.replaceAll("\\[domainNameVar\\]", this.firstLowCase(this.name));
		content = content.replaceAll("\\[domainNameVarHythen\\]", this.lowerSeperateWord(this.name,"-"));
		return content;
	}

	/**
	 * 将驼峰标识用 seperator 分隔开
	 * @param name2
	 * @param separeter
	 * @return
	 */
	private String lowerSeperateWord(String name2,String separetor) {
		String newChar = "";
		for(int i=0; i<name2.length(); i++){
	        char c = name2.charAt(i);
	        if((c >= 97 && c <= 122) || i==0) {
	        	newChar+=String.valueOf(c).toLowerCase();
	        }else{
	        	newChar+=separetor+String.valueOf(c).toLowerCase();
	        }
	    }
		return newChar;
	}

	public String generateMapStructCode() {
		String userMapper ="";
		for (CodeRelation codeRelation : this.relations) {
			String entity = codeRelation.getToEntity();
			if(!entity.equals(this.name)){
				userMapper+=entity+"Mapper.class,";
			}
		}
		int index = userMapper.lastIndexOf(",");
		if( index != -1 ){
			userMapper = userMapper.substring(0, userMapper.lastIndexOf(","));
		}
		String code=
		"package com.thtf.app.service.mapper;\n"+

		"import java.util.*;\n"+
		
		"import org.mapstruct.*;\n"+
		"import com.thtf.app.domain.*;\n"+
		"import com.thtf.app.service.dto.*;\n"+

		"/**\n"+
		" * Mapper for the entity "+this.name+" and its DTO called "+this.name+"DTO.\n"+
		" *\n"+
		" * Normal mappers are generated using MapStruct, this one is hand-coded as\n"+
		" * MapStruct support is still in beta, and requires a manual step with an IDE.\n"+
		" */\n"+
		"@Mapper(componentModel = \"spring\", uses = {"+userMapper+"})\n"+
		"public interface "+this.name+"Mapper extends EntityMapper<"+this.name+"DTO, "+this.name+"> {\n";
		
		
		for (CodeRelation codeRelation : this.relations) {
			if(codeRelation.isManyToOne()){
				code+="@Mapping(source = \""+codeRelation.getFromName()+".id\", target = \""+codeRelation.getFromName()+"Id\")\n";
			}
		}
		
	    code+=this.name+"DTO toDto("+this.name+" "+this.firstLowCase(this.name)+");\n";
	    
	    for (CodeRelation codeRelation : this.relations) {
			if(codeRelation.isManyToOne()){
				code+="@Mapping(source = \""+codeRelation.getFromName()+"Id\", target = \""+codeRelation.getFromName()+".id\""+")\n";
			}
		}
		
	    code+=this.name+" toEntity("+this.name+"DTO "+this.firstLowCase(this.name)+"DTO );\n";		
		code+=
		
	  	"default "+this.name+" fromId(Long id) {\n"+
		"        if (id == null) {\n"+
		"            return null;\n"+
		"        }\n"+
		"        "+this.name+" "+this.firstLowCase(this.name)+" = new "+this.name+"();\n"+
		"        "+this.firstLowCase(this.name)+".setId(id);\n"+
		"        return "+this.firstLowCase(this.name)+";\n"+
		"    }\n"+
		"}";
			return code;
		}
	
	

	public String generateDTOCode(){
		AppenderUtil appender = AppenderUtil.newOne();
		appender.append("package "+this.basePackage+".service.dto;");
		appender.append("");
		appender.append("import java.time.ZonedDateTime;");
		appender.append("import java.util.*;");
		appender.append("import javax.validation.constraints.*;");
		appender.append("public class "+this.name+"DTO {");
		String fieldTag = "//generate field by CodeGen！";
		String getSetTag = "//generate GetSet by CodeGen！";
		appender.append("");
		appender.append("private Long id;");
	    for (CodeField codeField : fields) {
			appender.append(codeField.generateDTOCodeField());
//			appender.append(fieldTag);
		}
	    
	    
	    for (CodeRelation relation : this.allRelations) {
	    	if(relation.isManyToOne() && relation.getFromEntity().equals(this.name)){
	    		appender.append(" private Long "+this.firstLowCase(relation.getFromName())+"Id ;");
	    	}
	    	if(relation.isOneToMany() && relation.getToEntity().equals(this.name)){
	    		appender.append(" private Long "+this.firstLowCase(relation.getToName())+"Id ;");
	    	}
	    	
	    	if(relation.isManyToMany() && relation.getFromEntity().equals(this.name)){
	    		appender.append(" private Long "+this.firstLowCase(relation.getFromName())+"Id ;");
	    	}
	    	if(relation.isOneToOne() && relation.getFromEntity().equals(this.name)){
	    		//TODO
	    	}
		}
	    appender.append(" public Long getId(){\n return id;\n}");
	    appender.append(" public void setId(Long id){\n this.id = id; \n}");
	    
	    for (CodeField codeField : fields) {
			appender.append(codeField.generateDTOCodeGetSet());
//			appender.append(getSetTag);
		}
	    
	    for (CodeRelation relation : this.allRelations) {
	    	if(relation.isManyToOne() && relation.getFromEntity().equals(this.name)){
	    		appender.append(this.genGetAndSet("Long", this.firstLowCase(relation.getFromName())+"Id"));
	    	}
	    	if(relation.isOneToMany() && relation.getToEntity().equals(this.name)){
	    		appender.append(this.genGetAndSet("Long", this.firstLowCase(relation.getToName()))+"Id");
	    	}
	    	
	    	if(relation.isManyToMany() && relation.getFromEntity().equals(this.name)){
	    		appender.append(this.genGetAndSet("Long", this.firstLowCase(relation.getFromName()))+"Id");
	    	}
	    	if(relation.isOneToOne() && relation.getFromEntity().equals(this.name)){
	    		//TODO
	    	}
		}
	    
		
		appender.append("}");
		
		return appender.toString();
		
	}

	/**
	 * @param appenderX
	 * @param relation
	 */
	private String genGetAndSet(String type,String name) {
		AppenderUtil appenderX = AppenderUtil.newOne();
		appenderX.append(" public "+type+" get"+this.firstUpperCase(name)+"(){ \n return this."+this.firstLowCase(name)+";\n}");
		appenderX.append(" public void set"+this.firstUpperCase(name)+"( "+type+" "+name+" ){\n"
				+"  this."+name+" = "+name+";\n"
				+"}");
		return appenderX.toString();
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
	
	

	public List<CodeRelation> getAllRelations() {
		return allRelations;
	}

	public void setAllRelations(List<CodeRelation> allRelations) {
		this.allRelations = allRelations;
	}

	@Override
	public String toString() {
		return "CodeEntity [name=" + name + ", tableName=" + tableName + "]";
	}
	
	private String firstUpperCase(String str){
		return str.substring(0,1).toUpperCase()+str.substring(1);
	}
	
	private String firstLowCase(String str) {
		return str.substring(0,1).toLowerCase()+str.substring(1);
	}

}
