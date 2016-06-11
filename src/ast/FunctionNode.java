package ast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wyil.lang.Code;
import wyil.lang.WyilFile.FunctionOrMethod;
import wyil.lang.Type;

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
	 * Children nodes of this collection
	 */
	private List<AbstractNode> children;

	private String name;
	private List<String> params;
	
	private int loopCounter; //Counter for loop labels

	protected FunctionNode(AbstractNode parent, FunctionOrMethod function) {
		super(parent);
		
		loopCounter = 0; 

		children = new ArrayList<AbstractNode>();
		variables = new HashSet<String>();
		params = loadParams(function.type());

		this.name = function.name();

		//Add label to variables
		variables.add(LABEL_VAR);

		//Function is going to have various subnodes which now need to be parsed, and strung together
		for (Code code : function.body().bytecodes()){
			children.addAll(createNodeFromCode(code, this));
		}
	}

	@Override
	public String translate() {
		String val = String.format("function %s(", name); //function name

		//Insert parameters
		for (int i = 0; i < params.size(); i++){
			val += params.get(i);

			if (i != params.size()-1){
				val += ",";
			}
		}

		//Open body
		val += ") { \n";

		//Variable declarations
		for (String s : variables){
			val += String.format("var %s; \n", s);
		}

		//Set default label case
		val += String.format("%s = '%s';\n", LABEL_VAR, DEFAULT_LABEL);

		//Start while loop and switch
		val += "while (true) { \n";
		val += String.format("switch(%s){\n", LABEL_VAR);

		//Add default switch case
		val += "case '"+DEFAULT_LABEL+"':\n";

		//Populate default case
		for (AbstractNode n : children){
			val += n.translate();
		}

		//Close all open brackets
		val += "}\n}\n}\n";
		return val;
	}

	@Override
	protected void addVariable(String var){
		variables.add(var);
	}
	
	protected String getLoopLabel(){
		String val = "loop"+loopCounter;
		loopCounter++;
		
		return val;
	}
	
	/**
	 * Returns the parameters available for a function
	 * 
	 * @param function
	 * @return
	 */
	private static List<String> loadParams(Type.FunctionOrMethod function){
		int numParams = function.params().size();
		List<String >params = new ArrayList<String>();

		//Params are numbered in order, so just put in the variable names
		for (int i = 0; i < numParams; i++){
			params.add(String.format("$%d", i));
		}

		return params;
	}
}
