package ast;

import jasm.lang.JvmType.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wyil.lang.Code;
import wyil.lang.WyilFile.FunctionOrMethod;

/**
 * A node to represent function declarations
 * 
 * @author Carl
 *
 */
public class FunctionNode extends AbstractNode {
	/**
	 * List of variables which are declared throughout the function.
	 * There are all declared at the start of the function
	 */
	private Set<String> variables;
	
	/**
	 * List of goto labels used throughout the function, and the node they go to.
	 * This is used for populating the switch statement
	 */
	private Map<String, LabelNode> labels;
	
	private Collection<AbstractNode> children;
	
	private String name;
	private List<String> params;

	protected FunctionNode(AbstractNode parent, FunctionOrMethod function) {
		super(parent);
		
		labels = new HashMap<String, LabelNode>();
		children = new ArrayList<AbstractNode>();
		variables = new HashSet<String>();
		
		params = new LinkedList<String>(); //TODO: Get actual parameters

		this.name = function.name();
		
		//Add label to variables, and set it to a default
		variables.add(LABEL_VAR);
		
		//Function is going to have various subnodes which now need to be parsed, and strung together
		for (Code code : function.body().bytecodes()){
			children.addAll(createNodeFromCode(code, this));
		}
		
		System.out.println(children.toString());
	}

	@Override
	public String evaluate() {
		String val = String.format("function %s(", name); //function name
		
		//Insert parameters
		for (int i = 0; i < params.size(); i++){
			val += params.get(i);
			
			if (i != params.size()-1){
				val += ",";
			}
		}
		
		//Open body
		val += ") { \n ";
		
		//Variable declarations
		for (String s : variables){
			val += String.format("var %s; \n", s);
		}
		
		//Set default label case
		val += String.format("%s = 'labelx';\n", LABEL_VAR);
		
		//Start while loop and switch
		val += "while (true) { \n";
		val += String.format("switch(%s){\n", LABEL_VAR);
		
		//Add default switch case
		val += "case 'labelx':\n";
		
		//Populate default case
		for (AbstractNode n : children){
			val += n.evaluate();
		}
		
		//Close all open brackets
		val += "}\n}\n}\n";
		return val;
	}
	
	@Override
	protected void addLabel(String label, LabelNode node){
		labels.put(label, node);
	}
	
	@Override
	protected void addVariable(String var){
		variables.add(var);
	}
}
