package main;

import ast.AbstractNode;
import whiley.io.File;

public class Whiley2Javascript {
	
	public static String convert(String filename){
		TreeBuilder builder = new TreeBuilder(filename);
		AbstractNode tree = builder.build();
		
		return tree.translate();
	}
	
	public static void main(String args[]){
		//TODO: Handle arguments
	}

}
