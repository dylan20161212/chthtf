package com.thtf.code.gen.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.thtf.code.gen.domain.CodeEntity;
import com.thtf.code.gen.parser.EntityParser;
import com.thtf.code.gen.util.AppenderUtil;

public class CodeEntityBuildFactory {
	
	private String jdlFile;
	private String projectPath;
	private String basePackage;
	private String templatePath;
	private List<CodeEntity> codeEntities = new ArrayList<CodeEntity>();
	private List<Map<String,Object>> relations = new ArrayList<Map<String,Object>>();
	
	/***
	 * 实例化构建工厂
	 * @param jdlFile
	 * @param projectPath
	 * @param basePackage
	 * @param templatePath
	 */
	public CodeEntityBuildFactory(String jdlFile, String projectPath,String basePackage,String templatePath) {
		super();
		this.jdlFile = jdlFile;
		this.basePackage = basePackage;
		this.projectPath = projectPath;
		this.templatePath = templatePath;
	}
	
	
	public CodeEntityBuildFactory build() throws FileNotFoundException, IOException{
		EntityParser ep = new EntityParser(this.jdlFile);
		ep.parser();
		List<Map<String,Object>> entities =  ep.getEntities();
		
		this.relations = ep.getRelations();
		
//		this.codeEntities =   (List<CodeEntity>) entities.stream().map((entity)->{
//			
//			List<Map<String,Object>> relation1s = (List<Map<String, Object>>) relations.stream().filter((relation)->{
//				Map<String,Object> from = (Map<String, Object>) relation.get("from");
//				return from.get("entity").equals(entity.get("name"));
//			});
//			return new CodeEntity(entity ,relation1s);
//		}).collect(Collectors.toList());
	
		for (Map<String,Object> entity : entities) {
			List<Map<String,Object>> relation1s = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> relation : relations) {
				Map<String, Object> from = (Map<String, Object>) relation.get("from");
				if(from.get("entity").equals(entity.get("name"))){
					relation1s.add(relation);
				}
			}
			
			this.codeEntities.add(new CodeEntity(this.templatePath,this.basePackage,entity ,relation1s,this.relations));
		}
		return this;
	}
	
	
	public void generateDomainCode(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String domainCode = codeEntity.generateDomainCode();
			System.out.println(domainCode);
			String fullName = this.getBaseFilePath()+"/domain/"+codeEntity.getName()+".java";
			this.writeToFile(fullName, domainCode);
		}
	}
	
	/**
	 * 获取类型文件基础路径
	 * @return
	 */
	private String getBaseFilePath(){
		return  this.projectPath+"/src/main/java/"+this.join(this.basePackage.split("\\."), "/");
	}
	
	public void generateLiqubase(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String code = codeEntity.generateLiquibase("xml");
			System.out.println(code);
//			String fullName = this.projectPath+"/src/main/java/"+this.join(this.basePackage.split("\\."), "/")+"/domains/"+codeEntity.getName()+".java";
//			this.writeToFile(fullName, domainCode);
		}
	}
	
	private CodeEntity getEntity(String entityName){
		CodeEntity entity = null;
		for (CodeEntity codeEntity : this.codeEntities) {
			if(entityName.equals(codeEntity.getName())){
				entity = codeEntity;
				break;
			}
		}
		return entity;
	}
	
	public void generateDBScript(){
		String allCode = "";
		for (CodeEntity codeEntity : this.codeEntities) {
			String code = codeEntity.generateDBScriptCode();
			allCode+="\n"+code;
			System.out.println(code);
		}
		for (Map<String, Object> relation : relations) {
			String relationName = (String) relation.get("key");
			String code = "";
			AppenderUtil appender = AppenderUtil.newOne();
			Map<String, Object> from = (Map<String, Object>) relation.get("from");
			Map<String, Object> to = (Map<String, Object>) relation.get("to");
			String fentityName =(String) from.get("entity");
			String tentityName =(String) to.get("entity");
			CodeEntity fentity = this.getEntity(fentityName);
			CodeEntity tentity = this.getEntity(tentityName);
			if(relationName.toLowerCase().equals("manytoone")){
				code = "alter table "+fentity.getTableName() +" add column "+tentityName.toLowerCase()+"_id BIGINT(200);";
				appender.append(code);
				code="alter table "+fentity.getTableName()+ " add constraint fk_"+fentityName.toLowerCase()+this.getNoNRepateNum()+" foreign key ("+tentityName.toLowerCase()+"_id)"+
						" REFERENCES "+tentity.getTableName()+"(id);"; 
				appender.append(code);
			}
			if(relationName.toLowerCase().equals("onetomany")){
				code = "alter table "+tentity.getTableName() +" add column "+fentityName.toLowerCase()+"_id BIGINT(200);";
				appender.append(code);
				code+="alter table "+tentity.getTableName()+ " add constraint fk_"+fentityName.toLowerCase()+this.getNoNRepateNum()+" foreign key ("+fentityName.toLowerCase()+"_id)"+
						" REFERENCES "+fentity.getTableName()+"(id);";
				appender.append(code);
			}
			if(relationName.toLowerCase().equals("manytomany")){
				code+="create table "+fentity.getTableName()+"_"+tentity.getTableName()+"(";
				appender.append(code);
		        code = " `"+fentityName.toLowerCase()+"_id` bigint(20) NOT NULL,";
		        appender.append(code);
		        code = " `"+tentityName.toLowerCase()+"_id` bigint(20) NOT NULL,";
		        appender.append(code);
		        appender.append(" PRIMARY KEY (`"+fentityName.toLowerCase()+"_id"+"`,`"+tentityName.toLowerCase()+"_id"+"`),");
		        appender.append(" CONSTRAINT `fk_middel_"+fentityName.toLowerCase()+"s_id"+this.getNoNRepateNum()+"` FOREIGN KEY (`"+fentityName.toLowerCase()+"_id`) REFERENCES `"+fentity.getTableName()+"` (`id`),");
		        appender.append(" CONSTRAINT `fk_middel_"+tentityName.toLowerCase()+"s_id"+this.getNoNRepateNum()+"` FOREIGN KEY (`"+tentityName.toLowerCase()+"_id`) REFERENCES `"+tentity.getTableName()+"` (`id`)");
		        appender.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		    }
			if(relationName.toLowerCase().equals("onetoone")){
				//TODO;
			}
			System.out.println(appender.toString());
			allCode+="\n"+code;
			
		}
		String fullName = this.projectPath+"/DBScript/"+(new Date()).getTime()+"full.sql";
		this.writeToFile(fullName, allCode);
	}
	
	private void writeToFile(String fullName,String content){
		File file = new File(fullName);
		int index = fullName.lastIndexOf("/");
		if(index !=-1 ){
			String path = fullName.substring(0,index);
			new File(path).mkdirs();
		}
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try(FileWriter fw = new FileWriter(fullName);)
		{
			fw.write(content);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void genRepository(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String code = codeEntity.generateRepositoryCode();
			String fullName = this.getBaseFilePath()+"/repository/"+codeEntity.getName()+"Repository.java";
			this.writeToFile(fullName, code);
		}
	}
	
	public void genDTO(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String code = codeEntity.generateDTOCode();
			System.out.println(code);
			String fullName = this.getBaseFilePath()+"/service/dto/"+codeEntity.getName()+"DTO.java";
			this.writeToFile(fullName, code);
		}
	}
	
	public void genMapper(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String code = codeEntity.generateMapStructCode();
			System.out.println(code);
			String fullName = this.getBaseFilePath()+"/service/mapper/"+codeEntity.getName()+"Mapper.java";
			this.writeToFile(fullName, code);
		}
	}
	
	public void genService(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String code = codeEntity.generateServiceCode();
			System.out.println(code);
			String fullName = this.getBaseFilePath()+"/service/"+codeEntity.getName()+"Service.java";
			this.writeToFile(fullName, code);
		}
	}
	
	public void genResource(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String code = codeEntity.generateResourceCode();
			System.out.println(code);
			String fullName = this.getBaseFilePath()+"/web/rest/"+codeEntity.getName()+"Resource.java";
			this.writeToFile(fullName, code);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String jdlFile="D:\\projects\\chthtf\\jdl\\new.jdl";
		String projectPath="D:\\projects\\backend\\redapple";
		String basePackage="com.thtf.app";
		String templatePath= "D:\\projects\\chthtf\\TPL";
		jdlFile = args[0];
		projectPath = args[1];
		basePackage = args[2];
		templatePath =args[3];
		boolean isGenDBScript = true;
		boolean isGenDomain = true;
		boolean isGenRepository = true;
		boolean isGenDTO = true;
		boolean isGenMapper = true;
		boolean isGenService = true;
		boolean isGenResource = true;
		
		CodeEntityBuildFactory builder = new CodeEntityBuildFactory(jdlFile,projectPath, basePackage,templatePath).build();
		if(isGenDBScript){
			builder.generateDBScript();
		}
		if(isGenDomain){
		  builder.generateDomainCode();
		}
		if(isGenRepository){
			  builder.genRepository();
		}
		if(isGenDTO){
			  builder.genDTO();
		}
		if(isGenMapper){
			  builder.genMapper();
		}
		if(isGenService){
			  builder.genService();
		}
		if(isGenResource){
			  builder.genResource();
		}
	}
	
	private String join(String[] strings,String seperator){
		String value = "";
		for (String string : strings) {
			value+=string+seperator;
		}
		
		if(value.endsWith(seperator)){
			return value.substring(0, value.lastIndexOf(seperator));
		}
		return value;
	}
	
	private String getNoNRepateNum(){
		Random r = new Random();
	    return  Math.abs(r.nextInt())+r.nextInt(100)+"";
		
	}
}
