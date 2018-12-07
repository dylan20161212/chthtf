package com.thtf.code.gen.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityParser {
	
	private String step1parttern1 = "^[0-9a-zA-Z_]{1,}$";

	//private String step2Pattern1 = "^\\(\\s*[0-9a-zA-Z_]{1,}\\s*\\)$";
	
	
//    private String filePath="D:/projects/backend/redapple/test.jdl";
    private String filePath="C:/Users/THTF/git/theJhi/doc/jdl/new.jdl";
    
    private String content="";
    
    private Integer position = 0;
    
    private Integer lastPosition = 0;
    
    private Map<String,String> dbTypeConstrainMap = new HashMap<String,String>();
    
    private List<Map<String,Object>> entities = new ArrayList<Map<String,Object>>();
    
    private List<Map<String,Object>> relations = new ArrayList<Map<String,Object>>();
    
    public EntityParser() {
		// TODO Auto-generated constructor stub
    	dbTypeConstrainMap.put("String", "required, minlength, maxlength, pattern, unique");
    	dbTypeConstrainMap.put("Integer", "required, min, max, unique");
    	dbTypeConstrainMap.put("Long", "required, min, max, unique");
    	dbTypeConstrainMap.put("BigDecimal", "required, min, max, unique");
    	dbTypeConstrainMap.put("Float", "required, min, max, unique");
    	dbTypeConstrainMap.put("Double", "required, min, max, unique");
    	dbTypeConstrainMap.put("Enum", "Enum");
    	dbTypeConstrainMap.put("Boolean", "required, unique");
    	dbTypeConstrainMap.put("LocalDate", "required, unique");
    	dbTypeConstrainMap.put("Date", "required, unique");
    	dbTypeConstrainMap.put("ZonedDateTime", "required, unique");
    	dbTypeConstrainMap.put("UUID", "required, unique");
    	dbTypeConstrainMap.put("Blob", "required, minbytes, maxbytes, unique");
    	dbTypeConstrainMap.put("AnyBlob", " required, minbytes, maxbytes, unique");
    	dbTypeConstrainMap.put("ImageBlob", "required, minbytes, maxbytes, unique");
    	dbTypeConstrainMap.put("TextBlob", "required, unique");
      	dbTypeConstrainMap.put("Instant", "required, unique");
	}


	public Boolean parser() throws FileNotFoundException, IOException {
		this.read();
		while(this.position<this.content.length()){
			String comment = getComment();
			String componentKey = this.getKeyValue().trim();
			if(componentKey.equals("entity")){			
				parseEntity(comment);	
			}
			
		    if(componentKey.equals("relationship")){
		    	this.parseRelationship(comment);
		    }
		}
		

	  System.out.println(entities);
	  System.out.println(this.relations);

		return false;
	}

	private void parseRelationship(String comment) {
		Map<String, Object> relation = new HashMap<String,Object>();
		relation.put("comment", comment);
		String relationKey = this.getKeyValue();
		if(isValidRelationName(relationKey)){
			relation.put("key", relationKey);
		}
		
		String value = this.getKeyValue();
		if(value.equals("{")){
			
			while(true){
			
					
					Map<String, Object> from = new HashMap<String,Object>();
					String fromName = this.getKeyValue();
					if(fromName.equals("}")){
						break;
					}
					if(this.isMatchName(fromName)){
						from.put("entiy", fromName);
						String leftB = this.getKeyValue();
						if(leftB.equals("{")){//关系的
							String relationName = this.getKeyValue();
							if(this.isMatchName(relationName)){
								from.put("relationName", relationName);
								if(this.getKeyValue().equals("(")){
									String displayName = this.getKeyValue();
									if(this.isMatchName(displayName)){
										from.put("display",displayName);
										relation.put("from", from);
										String firstEnd = this.getKeyValue();
										String lastEnd = this.getKeyValue();
										if(!(firstEnd.equals(")"))&&!(lastEnd.equals("}"))){
											throw new RuntimeException("parse error");
										}
									}
									
									
								}
							}
							
							
						}
						if(leftB.equals("to")||this.getKeyValue().equals("to")){
							Map<String, Object> to = new HashMap<String,Object>();
							String toEntityName = this.getKeyValue();
							if(toEntityName.endsWith(",")){
								toEntityName = toEntityName.substring(0, toEntityName.indexOf(","));
								to.put("entiy", toEntityName);
								relation.put("to", to);
								this.relations.add(relation);
								continue;
							}else if(this.getKeyValue().trim().equals("}")){
								this.relations.add(relation);
								break;
							}else{
								this.position = this.lastPosition;
							}
							if(this.isMatchName(toEntityName)){
								to.put("entiy", toEntityName);
								String leftR = this.getKeyValue();
								if(leftR.equals("{")){//关系的
									String relationName = this.getKeyValue();
									if(this.isMatchName(relationName)){
										to.put("relationName", relationName);
										if(this.getKeyValue().equals("(")){
											String displayName = this.getKeyValue();
											if(this.isMatchName(displayName)){
												to.put("display",displayName);
												relation.put("to", to);
												this.relations.add(relation);
												String firstEnd = this.getKeyValue();
												String lastEnd = this.getKeyValue();
												if(!(firstEnd.equals(")"))&&!(lastEnd.equals("}"))){
													throw new RuntimeException("parse error");
												}
											}
											
											
										}
									}
								}
							}
							
							
						}else{
							throw new RuntimeException("parse error,after relationName!");
						}
						
					}
					
					
					
					//while end
				}
			
			
			
		}	
//		if(value.equals(anObject))
		
		
		
	}


	/**
	 * 
	 */
	private boolean isValidRelationName(String relationName) {
		String[] realtions = new String[] {"OneToOne","ManyToOne","OneToMany","ManyToMany"};
		
		for (String relation : realtions) {
			if(relationName.toString().equals(relation)){
				return true;
			}
		}
		throw new RuntimeException("invalid realtionName:"+relationName);
	}


	/**
	 * @param entity 
	 * 
	 */
	private void parseEntity(String comment) {
		
		Map<String, Object> entity = new HashMap<String,Object>();
		entity.put("comment", comment);
	
		String entityName = this.getKeyValue();
		isMatchName(entityName);
		//System.out.println("entity name:"+entityName);
		entity.put("name", entityName);
		String leftQ = this.getKeyValue();
		if(leftQ.equals("(")){
			String tableName = "";
			String v = "";
			while(!(v=this.getKeyValue()).equals(")")){
				tableName+=v;
			}
			//System.out.println("table name: "+tableName);
			entity.put("tableName", tableName);
		}
		String leftB = this.getKeyValue();
		if(leftB.equals("{")){
//			String v = "";
			List<Map<String,Object>> fields = new ArrayList<Map<String,Object>>();
			do{
				Map<String,Object> field = new HashMap<String,Object>();
				
				String fieldComment = this.getComment();
				field.put("comment",fieldComment);
				String fieldStr = "";
				
				String value = "";
				
				while(!value.endsWith(",")){
					if(value.endsWith("}")){
						fieldStr+=value.substring(0, value.length()-1)+" ";
						break;
					}
					value = this.getKeyValue().trim();
					fieldStr+=value+" ";												
				}
				if(fieldStr.trim().endsWith("}")){
					fieldStr=fieldStr.trim().substring(0, fieldStr.trim().length()-1);
				}
				String[] vs = fieldStr.split("\\s");
				this.processEntityField(vs,field,entity,fields);
				if(value.endsWith("}")){
					entity.put("fields",fields);
					this.entities.add(entity);
					break;
				}
//				System.out.println("filed comments:"+fieldComment);
//				System.out.println("field : "+fieldStr);		
			}while(true);
		}
	}



	private boolean processEntityField(String[] vs, Map<String, Object> field, Map<String, Object> entity, List<Map<String, Object>> fields) {
		if(this.isMatchName(vs[0])){
			field.put("name", vs[0]);
		}
		String type = vs[1].trim();
		
		if(type.endsWith(",")){
			type= type.substring(0, type.length()-1);
		}
		
		String constrain = this.dbTypeConstrainMap.get(type);
		if(constrain != null){
			String feildConstrain = "";
			for(int i=2;i<vs.length;i++){
				if(!constrain.contains(vs[i])){
					throw new RuntimeException(constrain+" parse error");
				}
				feildConstrain+=vs[i];
			}
			field.put("type", type);
			field.put("constrain", feildConstrain);
			fields.add(field);
			
		}else{
			throw new RuntimeException(type+" parse error");
		}
		return false;
	}

	/**
	 * @param entityName
	 */
	private boolean isMatchName(String entityName) {
		if(entityName.matches(this.step1parttern1)){
			//System.out.println(entityName);
			return true;
		}else{
			throw new RuntimeException("parse error");
		}
	}
	
	

	/**
	 * @return
	 */
	private String getComment() {
		String value = this.getKeyValue();
		String comment = "";
		if(value.equals("/*")){
			comment = "/*";
			String endComment ="" ;
			while(!(endComment=this.getKeyValue()).equals("*/")){
				comment+=endComment;
			}
			comment+="*/";
			//System.out.println(comment);
		}else{
			this.position = this.lastPosition;
		}
		return comment;
	}
	
	
	
	private String[] specialCharactors =new String[]{"/*","*/","(",")","{","}"};
	
	
	private String getKeyValue(){
		this.lastPosition = this.position;
		int len = this.content.length();
		StringBuffer sb = new StringBuffer();
		while(position < len){
			char c = this.content.charAt(position);
			sb.append(c);
			String ch = sb.toString().trim();
			if(this.isContianedSpecial(ch.trim())){
				position++;
				return ch.trim();
			}
			String x = this.getChWithEndWithSpecial(ch);
			if(x!=null){
				
				return x;
			}
			if(String.valueOf(c).equals(" ") && !ch.equals("")){
				position++;
				return ch;
			}
			position++;
		}
		return null;
		
	}
	
	private boolean isContianedSpecial(String sb){
		for (String string : specialCharactors) {
			if(sb.equals(string)){
				return true;
			}
		}
		return false;
	}
	
	
	private String getChWithEndWithSpecial(String sb){
		for (String string : specialCharactors) {
			if(sb.endsWith(string)){
				position++;
				this.position=this.position-string.length();
				return sb.substring(0, sb.indexOf(string)).trim();
			}
		}
		return null;
	}
	
	
	public String read() throws FileNotFoundException, IOException{
	 
		if(!this.content.isEmpty()){
			return this.content;
		}
		try (InputStream is = new FileInputStream(this.filePath);
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);) {

			String elementstr = null;
			while ((elementstr = br.readLine()) != null) {
				this.content += elementstr+" ";
			}
		}
		return this.content;
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String content="entity A (t_test) { name String, age Integer }";
		EntityParser ep = new EntityParser();
	    ep.parser();
	}

}
