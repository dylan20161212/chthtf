package com.thtf.code.gen;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Test {
	public static void main(String[] args) {
		 PasswordEncoder pe = new BCryptPasswordEncoder();
		 System.out.println(pe.encode("admin") );
	}
}
