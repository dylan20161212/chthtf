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

	// private String step2Pattern1 = "^\\(\\s*[0-9a-zA-Z_]{1,}\\s*\\)$";

	// private String filePath="D:/projects/backend/redapple/test.jdl";
	
	private String filePath;

	private String content = "";

	private Integer position = 0;

	private Integer lastPosition = 0;

	private Map<String, String> dbTypeConstrainMap = new HashMap<String, String>();

	private List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();

	private List<Map<String, Object>> relations = new ArrayList<Map<String, Object>>();

	private Integer lineNum = 0;

	public EntityParser(String jdlFile) {
		// TODO Auto-generated constructor stub
		if(jdlFile == null || jdlFile.trim().equals("")){
			throw new RuntimeException("jdlFile can not is empty!");
		}
		this.filePath = jdlFile;
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
		try{
			this.read();
			while (this.position < this.content.length()) {
				String comment = getComment();
				String keyValue = this.getKeyValue();
				keyValue = ((keyValue == null)?"":keyValue);
				String componentKey = keyValue.trim();
				if (componentKey.equals("entity")) {
					parseEntity(comment);
				}
	
				if (componentKey.equals("relationship")) {
					this.parseRelationship(comment);
				}
			}
	
			System.out.println(entities);
			System.out.println(this.relations);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("parse error at line:"+this.lineNum +" or upper line！");
		}

		return false;
	}

	private void parseRelationship(String comment) {
		Map<String, Object> relation = new HashMap<String, Object>();
		relation.put("comment", comment);
		String relationKey = this.getKeyValue();
		if (isValidRelationName(relationKey)) {

		}

		String value = this.getKeyValue();
		if (value.equals("{")) {
			String relationName = "";
			while (true) {

				Map<String, Object> from = new HashMap<String, Object>();
				String fromName = this.getKeyValue().trim();
				if (fromName.equals("}")) {
					break;
				}
				if (fromName.equals(",")) {
					continue;
				}
				if (this.isMatchName(fromName)) {
					relation = new HashMap<String, Object>();
					relation.put("comment", comment);
					relation.put("key", relationKey);
					from.put("entity", fromName);
					relation.put("from", from);
					String leftB = this.getKeyValue();
					if (leftB.equals("{")) {// 关系的
						relationName = this.getKeyValue();
						if (this.isMatchName(relationName)) {
							from.put("relationName", relationName);
							if (this.getKeyValue().equals("(")) {
								String displayName = this.getKeyValue();
								if (this.isMatchName(displayName)) {
									from.put("display", displayName);
									// relation.put("from", from);
									String firstEnd = this.getKeyValue();
									String lastEnd = this.getKeyValue();
									if (!(firstEnd.equals(")")) && !(lastEnd.equals("}"))) {
										throw new RuntimeException("parse error at: " + this.lineNum);
									}
								}

							}
						}

					}
					if (leftB.equals("to") || this.getKeyValue().equals("to")) {
						Map<String, Object> to = new HashMap<String, Object>();
						String toEntityName = this.getKeyValue();
						relation.put("to", to);
						this.relations.add(relation);
						if (toEntityName.endsWith(",")) {
							toEntityName = toEntityName.substring(0, toEntityName.indexOf(","));
							to.put("entity", toEntityName);
							continue;
						} else if (this.getKeyValue().trim().equals("}")) {
							to.put("entity", toEntityName);
							// this.relations.add(relation);
							break;
						} else {
							this.position = this.lastPosition;
						}
						if (this.isMatchName(toEntityName)) {
							to.put("entity", toEntityName);
							String leftR = this.getKeyValue();
							if (leftR.equals("{")) {// 关系的
								relationName = this.getKeyValue();
								if (this.isMatchName(relationName)) {
									relation.put("to", to);
									// this.relations.add(relation);
									to.put("relationName", relationName);
									if (this.getKeyValue().equals("(")) {
										String displayName = this.getKeyValue();
										if (this.isMatchName(displayName)) {
											to.put("display", displayName);
											// relation.put("to", to);
											// this.relations.add(relation);
											String firstEnd = this.getKeyValue();
											String lastEnd = this.getKeyValue();
											if (!(firstEnd.equals(")")) && !(lastEnd.equals("}"))) {
												throw new RuntimeException("parse error at: " + this.lineNum);
											}
										}

									}
								}
							}
						}

					} else {
						throw new RuntimeException("parse error,after relationName "+relationName+" at:" + this.lineNum);
					}

				}

				// while end
			}

		}
		// if(value.equals(anObject))

	}

	/**
	 * 
	 */
	private boolean isValidRelationName(String relationName) {
		String[] realtions = new String[] { "OneToOne", "ManyToOne", "OneToMany", "ManyToMany" };

		for (String relation : realtions) {
			if (relationName.toString().equals(relation)) {
				return true;
			}
		}
		throw new RuntimeException("invalid realtionName:" + relationName + ",at:" + this.lineNum);
	}

	/**
	 * @param entity
	 * 
	 */
	private void parseEntity(String comment) {

		Map<String, Object> entity = new HashMap<String, Object>();
		entity.put("comment", comment);
		this.entities.add(entity);
		String entityName = this.getKeyValue();
		isMatchName(entityName);
		// System.out.println("entity name:"+entityName);
		entity.put("name", entityName);
		String leftQ = this.getKeyValue();
		if(leftQ.equals("entity") || leftQ.equals("relationship")){//如果获取到的是其中之一，那么回到上一个位置，重新走此方法解析entity
			this.position = lastPosition;
			return;
		}
		
		if (leftQ.equals("(")) {// 如果是小括号，获取括号内表名
			String tableName = "";
			String v = "";
			while (!(v = this.getKeyValue()).equals(")")) {
				tableName += v;
			}
			// System.out.println("table name: "+tableName);
			entity.put("tableName", tableName);
		}
		
		if (leftQ.equals("{")) {// 如果括号是大括号，证明没有定义表名，所以回到上个位置重新走大括号逻辑
			this.position = this.lastPosition;
		}
		
		String leftB = this.getKeyValue().trim();
		
		if (leftB.equals("{")) {//解析字段内容
			// String v = "";
			List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
			do {
				Map<String, Object> field = new HashMap<String, Object>();

				String fieldComment = this.getComment();
				field.put("comment", fieldComment);
				String fieldStr = "";

				String value = "";

				while (!value.endsWith(",")) {
					if (value.endsWith("}")) {//证明此entity解析完成
						fieldStr += value.substring(0, value.length() - 1) + " ";
						break;
					}
					value = this.getKeyValue().trim();
					fieldStr += value + " ";
				}
				if (fieldStr.trim().endsWith("}")) {
					fieldStr = fieldStr.trim().substring(0, fieldStr.trim().length() - 1);
				}
				String[] vs = fieldStr.split("\\s");
				this.processEntityField(vs, field, entity, fields);
				if (value.endsWith("}")) {
					entity.put("fields", fields);
//					this.entities.add(entity);
					break;
				}
				// System.out.println("filed comments:"+fieldComment);
				// System.out.println("field : "+fieldStr);
			} while (true);
		}
	}

	private boolean processEntityField(String[] vs, Map<String, Object> field, Map<String, Object> entity,
			List<Map<String, Object>> fields) {
		if (this.isMatchName(vs[0])) {
			field.put("name", vs[0]);
		}
		String type = vs[1].trim();

		if (type.endsWith(",")) {
			type = type.substring(0, type.length() - 1);
		}

		String constrain = this.dbTypeConstrainMap.get(type);
		if (constrain != null) {
			String feildConstrain = "";
			for (int i = 2; i < vs.length; i++) {
				// if(!constrain.contains(vs[i])){
				// throw new RuntimeException(constrain+" parse error
				// at:"+this.lineNum);
				// }
				if (vs[i].endsWith(",")) {
					vs[i]= vs[i].substring(0, vs[i].lastIndexOf(","));
				}
				if (vs[i].equals(",")) {
					feildConstrain = feildConstrain.trim();
				}
				if (vs[i].equals(")")) {
					feildConstrain += vs[i] + " ";
				} else {

					feildConstrain += vs[i];
				}

			}
			field.put("type", type);
			field.put("constrain", feildConstrain);
			fields.add(field);

		} else {
			throw new RuntimeException(type + " parse error at:" + this.lineNum);
		}
		return false;
	}

	/**
	 * @param entityName
	 */
	private boolean isMatchName(String entityName) {
		if (entityName.matches(this.step1parttern1)) {
			// System.out.println(entityName);
			return true;
		} else {
			throw new RuntimeException("parse error at:" + this.lineNum);
		}
	}

	/**
	 * @return
	 */
	private String getComment() {
		String value = this.getKeyValue();
		String comment = "";
		if(value == null){
			return "";
		}
		if (value.equals("/*")) {
			// comment = "/*";
			String endComment = "";
			while (!(endComment = this.getKeyValue()).equals("*/")) {
				comment += endComment;
			}
			// comment+="*/";
			// System.out.println(comment);
		} else {
			this.position = this.lastPosition;
		}
		return comment;
	}

	private String[] specialCharactors = new String[] { "/*", "*/", "(", ")", "{", "}" };

	private String getKeyValue() {
		this.lastPosition = this.position;
		int len = this.content.length();
		StringBuffer sb = new StringBuffer();
		while (position < len) {
			char c = this.content.charAt(position);
			sb.append(c);
			String ch = sb.toString().trim();
			this.setLineNum();
			if (this.isContianedSpecial(ch.trim())) {
				position++;
				return ch.trim();
			}
			String x = this.getChWithEndWithSpecial(ch);
			if (x != null) {

				return x;
			}

			if (String.valueOf(c).equals(" ") && !ch.equals("")) {
				position++;
				return ch;
			}
			position++;
		}
		return null;

	}
	
	private void setLineNum(){
		
		try{
			if(this.position == 0){
				return;
			}
			String str = String.valueOf(this.content.charAt(this.position));
			if(str.equals("\n")){
				this.lineNum++;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private boolean isContianedSpecial(String sb) {
		for (String string : specialCharactors) {
			if (sb.equals(string)) {
				return true;
			}
		}
		return false;
	}

	private String getChWithEndWithSpecial(String sb) {
		for (String string : specialCharactors) {
			if (sb.endsWith(string)) {
				position++;
				this.position = this.position - string.length();
				return sb.substring(0, sb.indexOf(string)).trim();
			}
		}
		return null;
	}

	public String read() throws FileNotFoundException, IOException {

		if (!this.content.isEmpty()) {
			return this.content;
		}
		try (InputStream is = new FileInputStream(this.filePath);
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);) {

			String elementstr = null;
			while ((elementstr = br.readLine()) != null) {
				this.content += elementstr + "\n ";
			}
		}
		return this.content;
	}

	public List<Map<String, Object>> getEntities() {
		return entities;
	}

	public void setEntities(List<Map<String, Object>> entities) {
		this.entities = entities;
	}

	public List<Map<String, Object>> getRelations() {
		return relations;
	}

	public void setRelations(List<Map<String, Object>> relations) {
		this.relations = relations;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
//		String path = "D:\\projects\\chthtf\\jdl\\deconflict.jdl";
		String path = "D:\\projects\\chthtf\\jdl\\new.jdl";
		String content = "entity A (t_test) { name String, age Integer }";
		EntityParser ep = new EntityParser(path);
		ep.parser();
	}

}
