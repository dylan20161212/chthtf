package com.thtf.code.gen.util;

public class AppenderUtil {
	private StringBuffer sb = new StringBuffer();
	private final String ENTER = "\n\r";
	
	public  AppenderUtil append(String appendCode){
		appendCode+=this.ENTER;
		sb.append(appendCode);
		return this;
	}
	
	public AppenderUtil delete(int start,int end){
		 sb.delete(start, end);
		 return this;
	}
	
	public AppenderUtil deleteLast(String simbo){
		 int start =  sb.toString().lastIndexOf(simbo);
		
		 sb.delete(start, start+1);
		 return this;
	}
	
    @Override
    public String toString() {
    	return this.sb.toString();
    }
    
    public static AppenderUtil newOne(){
    	return new AppenderUtil();
    }
}
