package com.thtf.code.gen.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeField {
	private String comment;
	private String name;
	private String type;
	private String constraint;
	private CodeEntity entity;
	
	private Map<String, String> typeToSQLType = new HashMap<String,String>(){
		{
			put("String", "VARCHAR(255)");
			put("Integer", "INTEGER");
			put("Long", "BIGINT");
			put("BigDecimal", "DECIMAL");
			put("Float", "REAL");
			put("Double", "FLOAT");
			put("Boolean", "BIT");
			put("LocalDate", "DATETIME");
			put("ZonedDateTime", "DATETIME");
			put("BLOB", "BLOB");
			put("Instant", "Instant");
			put("TextBlob","CLOB");
		}
	};
	
	private Map<String, String> validateMap = new HashMap<String,String>(){
		{
			put("required", "@NotNull");
			put("minlength", "@Length");
			put("pattern", "@pattern");
			put("unique", "unique");
			put("min", "@Min");
			put("max", "@Max");		
		}
	};
	

	public CodeField(Map<String, Object> field, CodeEntity codeEntity) {
		this.comment = (String) field.get("comment");
		this.name = (String) field.get("name");
		this.type = (String) field.get("type");
		this.constraint = (String) field.get("constrain");
		this.entity = codeEntity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConstraint() {
		return constraint;
	}

	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String generateDomainCodeField() {
		String constraint = "";
		String javaxValidate = "";
		if(this.constraint.contains("required")){
			constraint = " NOT NULL ";
			javaxValidate += " @NotNull(message=\""+this.name+" can not be empty! \") \n";
		}
		if(this.constraint.contains("unique")){
			constraint = " unique ";
		}
		
		if(this.constraint.contains("pattern")){
			String pattern = "*";
			javaxValidate += " @Pattern("+pattern+")\n";
		}
		
		if(this.constraint.contains("min(")){
			String min = this.getContentByRegx(this.constraint, "min\\((\\d+)\\)").get(0);
			javaxValidate += " @Min("+min+")\n";
		}
		
		if(this.constraint.contains("max(")){
			String max = this.getContentByRegx(this.constraint, "max\\((\\d+)\\)").get(0);
			javaxValidate += " @Max("+max+")\n";
		}
		
		if(this.constraint.contains("minlength") && this.constraint.contains("maxlength")){
			
			String min = this.getContentByRegx(this.constraint, "minlength\\((\\d+)\\)").get(0);
			String max = this.getContentByRegx(this.constraint, "maxlength\\((\\d+)\\)").get(0);
			javaxValidate += " @Length(min= "+min+" , max= " +max+", message=\""+this.name+" lenght must more than "+min+" and less than "+max+"! \") \n";
		}
		
		else if(this.constraint.contains("minlength")){
			String min = this.getContentByRegx(this.constraint, "minlength\\((\\d+)\\)").get(0);
			javaxValidate += " @Length(min= "+min+" ,message=\""+this.name+" lenght must more than "+min+" ! \") \n";
		}
		
		else if(this.constraint.contains("maxlength")){
			String max = this.getContentByRegx(this.constraint, "maxlength\\((\\d+)\\)").get(0);;
			javaxValidate += " @Length(max= "+max+" ,message=\""+this.name+" lenght must less than "+max+" ! \") \n";
		}
		
		
			
			
			return 
					
					javaxValidate+"  @Column(name = \""+this.name+"\",columnDefinition=\""+this.typeToSQLType.get(this.type)+ constraint+" COMMENT '"+this.comment+"'\") \n"+
		       "  private "+this.type+" " +this.name+"; \n";
		
		 
	}
	
	public static List<String> getContentByRegx(String value,String regx){
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(value);
		List<String> list = new ArrayList<String>();
		int i = 1;
		while(m.find()){
			list.add(m.group(1));
			
		}
		return list;
	}
	
	public static void main(String[] args) {
		getContentByRegx("minlength(30) maxlength(40)","minlength\\((\\d+)\\)");
	}
	
   public String generateDomainCodeGetSet() {
		String nextLine = "\n";
		return  "  public "+this.type+" get"+this.name.substring(0, 1).toUpperCase()+this.name.substring(1)+"() {"+nextLine+
				"  	  return "+this.name+";"+nextLine+
				"  }"+nextLine+nextLine+
				
				"  public void set"+this.name.substring(0, 1).toUpperCase()+this.name.substring(1)+"("+this.type+" "+this.name+") {"+
				"	  this."+this.name+" = "+this.name+";"+
				"  }"+nextLine+nextLine;
				
				
	}

}
