package main;

import ast.AbstractNode;

public class Whiley2Javascript {

	public static String convert(String filename){
		TreeBuilder builder = new TreeBuilder(filename);
		AbstractNode tree = builder.build();

		return tree.translate();
	}

	public static void main(String args[]){
		if (args.length == 0){
			System.out.println("Target whiley file required");
			return;
		}

		//For now, they can pipe it to an output file if they want
		System.out.println(convert(args[0]));
	}
}
