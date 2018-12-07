package com.thtf.code.gen.factory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.thtf.code.gen.domain.CodeEntity;

public class CodeEntityBuildFactory {
	private String codePath;
	private String basePackage;
	private String fullJDLName;
	
	

	public CodeEntityBuildFactory(String codePath, String basePackage, String fullJDLName) {
		super();
		this.codePath = codePath;
		this.basePackage = basePackage;
		this.fullJDLName = fullJDLName;
	}

	public List<CodeEntity> parseJdlToEntity() throws FileNotFoundException, IOException {
		List<CodeEntity> entities = new ArrayList<CodeEntity>();
		
		String[] elements = this.elements();
		// TODO
		

		return entities;
	}

	private String[] elements() throws FileNotFoundException, IOException {
		File jdl = new File(this.fullJDLName);
		String allStr = null;
		try (InputStream is = new FileInputStream(jdl);
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);) {

			String elementstr = null;
			while ((elementstr = br.readLine()) != null) {
				allStr += elementstr;
			}
		}
		if (allStr != null) {
			return allStr.split("\\s+");
		} else {
			return null;
		}

	}

	public static void main(String[] args) {
//		String x = "x sfa    sdfafe     eee";
//		String[] xs = x.split("\\s+");
//		for (String string : xs) {
//			System.out.println(string);
//		}
		
		
	}
}
