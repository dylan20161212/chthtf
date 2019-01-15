package com.thtf.code.gen.domain;

import com.thtf.code.gen.util.AppenderUtil;

public class CodeRelation {
	private String comment;

	private String name;

	private String fromName;
	private String fromEntity;
	private String fromDisplay;

	private String toName;
	private String toEntity;
	private String toDisplay;
	
	private CodeEntity codeEntity;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromEntity() {
		return fromEntity;
	}

	public void setFromEntity(String fromEntity) {
		this.fromEntity = fromEntity;
	}

	public String getFromDisplay() {
		return fromDisplay;
	}

	public void setFromDisplay(String fromDisplay) {
		this.fromDisplay = fromDisplay;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToEntity() {
		return toEntity;
	}

	public void setToEntity(String toEntity) {
		this.toEntity = toEntity;
	}

	public String getToDisplay() {
		return toDisplay;
	}

	public void setToDisplay(String toDisplay) {
		this.toDisplay = toDisplay;
	}

	public String generateDomainCode() {
		String code = "";
		String toName = "";
		
		if(this.fromName == null || this.fromName.isEmpty()){
			toName = this.toEntity.substring(0, 1).toLowerCase()+this.toEntity.substring(1);
		}else{
			toName = this.fromName;
		}
		if(isManyToOne()){
			code = "   @ManyToOne \n"+
		           "   private "+this.toEntity +" "+toName+" ;";
			
		}
		if(isOneToMany()){
			code = "   @OneToMany(mappedBy = \""+this.fromName+"\")\n"+
			              "private Set<"+this.toEntity +"> "+((toName.length()==this.toEntity.length())?toName+"s":toName)+" = new HashSet<"+this.toEntity+">();";
			setImportLib();
		}
		
		if(isOneToOne()){
			code = "   @ManyToOne+\n"+
		           "   private "+this.toEntity +" "+toName+" ;";
		}
		
		if(isManyToMany()){
			code = "   @ManyToMany \n"+
				    //"@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)\n"+
				   
				   "   private Set<"+this.toEntity+"> "+((toName.length()==this.toEntity.length())?toName+"s":toName)+" = new HashSet<"+this.toEntity+">();";
			setImportLib();
		}
		return code+"\n";
	}

	/**
	 * @return
	 */
	public boolean isManyToMany() {
		return this.name.equals("ManyToMany");
	}

	/**
	 * @return
	 */
	public boolean isOneToOne() {
		return this.name.equals("OneToOne");
	}

	/**
	 * @return
	 */
	public boolean isOneToMany() {
		return this.name.equals("OneToMany");
	}

	/**
	 * @return
	 */
	public boolean isManyToOne() {
		return this.name.equals("ManyToOne");
	}
	
	

	/**
	 * 
	 */
	private void setImportLib() {
		String set = "import java.util.Set";
		String hashSet  = "import java.util.HashSet";
		String importLib = this.codeEntity.getImportLib();
		if(importLib == null){
			importLib = "";
		}
		if(!importLib.contains(set)){
			importLib+="\n "+set+";\n ";
		}
		if(!importLib.contains(hashSet)){
			importLib+="\n "+hashSet+";\n ";
		}
		
		
		this.codeEntity.setImportLib(importLib);
	}

	public void setCodeEntity(CodeEntity codeEntity) {
		this.codeEntity = codeEntity;
	}

	public String generateDBScriptCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String generateDomainGetSetCode() {
		String code = "";
		String toName = "";
		if(this.fromName == null || this.fromName.isEmpty()){
			toName = this.toEntity.substring(0, 1).toLowerCase()+this.toEntity.substring(1);
		}else{
			toName = this.fromName;
		}
		String methodName= toName.substring(0, 1).toUpperCase()+toName.substring(1);
		code +=" public "+toEntity+" get"+methodName+" "+"( ){\n"+
		       "     return this."+toName+"; \n}\n";
		
		code+=" public void set"+methodName+"("+toEntity +" "+toName+" ){\n this."+toName+" = "+toName+";\n}\n";
		
		return code;
	}

	
	
	
	

	
	
	
	

}
