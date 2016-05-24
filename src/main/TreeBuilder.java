package main;

import java.io.IOException;

import wyil.io.WyilFileReader;
import wyil.lang.WyilFile;
import ast.*;

/**
 * Builds an Abstract syntax tree from a Wyil input
 * @author Carl
 *
 */
public class TreeBuilder {
	private WyilFile file;
	
	public TreeBuilder(String filename){
		try {
			WyilFileReader reader = new WyilFileReader(filename);
			file = reader.read();
		} catch (IOException e){
			throw new RuntimeException("Could not load the WyIL File");
		}
	}
	
	public AbstractNode build(){
		return new ProgramNode(file);
	}
}
