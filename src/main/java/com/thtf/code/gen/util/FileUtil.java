package com.thtf.code.gen.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {
	
	
	public static String read(String path) {

		String content = "";
		try (InputStream is = new FileInputStream(path);
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);) {

			String elementstr = null;
			while ((elementstr = br.readLine()) != null) {
				content += elementstr + "\n ";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
}
