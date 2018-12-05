package com.thtf.app;

import java.util.function.Function;

public class Test {
	/*public static void main(String[] args) {
		Function<String,Integer> valueOf = Integer::valueOf;
		Function<String,String> ret= valueOf.andThen(String::valueOf);
		print(ret.apply("100"));
	}
	*/
	public static void print(Object o){
		System.out.println(o);
	}
}
