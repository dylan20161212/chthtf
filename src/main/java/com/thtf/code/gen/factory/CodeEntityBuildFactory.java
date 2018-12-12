package com.thtf.code.gen.factory;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.thtf.code.gen.domain.CodeEntity;
import com.thtf.code.gen.parser.EntityParser;

public class CodeEntityBuildFactory {
	
	private String jdlFile;
	private String projectPath;
	private String basePackage;
	private List<CodeEntity> codeEntities = new ArrayList<CodeEntity>();
	public CodeEntityBuildFactory(String jdlFile, String projectPath,String basePackage) {
		super();
		this.jdlFile = jdlFile;
		this.basePackage = basePackage;
		this.projectPath = projectPath;
	}
	
	public CodeEntityBuildFactory build() throws FileNotFoundException, IOException{
		EntityParser ep = new EntityParser();
		ep.parser();
		List<Map<String,Object>> entities =  ep.getEntities();
		
		List<Map<String,Object>> relations = ep.getRelations();
		
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
			
			this.codeEntities.add(new CodeEntity(entity ,relation1s));
		}
		return this;
	}
	
	
	public void generateCode(){
		for (CodeEntity codeEntity : this.codeEntities) {
			String domainCode = codeEntity.generateDomainCode();
			System.out.println(domainCode);
			String fullName = this.projectPath+"/src/main/java/"+this.join(this.basePackage.split("\\."), "/")+"/domains/"+codeEntity.getName()+".java";
			this.writeToFile(fullName, domainCode);
		}
	}
	
	private void writeToFile(String fullName,String content){
		try(FileWriter fw = new FileWriter(fullName);)
		{
			fw.write(content);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		CodeEntityBuildFactory builder = new CodeEntityBuildFactory("","D:/projects/chthtf", "com.thtf.app");
		builder.build().generateCode();
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
}
