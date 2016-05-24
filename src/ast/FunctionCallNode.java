package ast;

import java.util.LinkedList;
import java.util.List;

import wyil.lang.Codes;

/**
 * A node for making calls to other functions in the program
 * 
 * @author Carl
 *
 */
public class FunctionCallNode extends AbstractNode {
	private List<String> params;
	private String function;
	
	protected FunctionCallNode(AbstractNode parent) {
		super(parent);
		
		params = new LinkedList<String>();
	}
	
	protected FunctionCallNode(AbstractNode parent, String function, List<String> params){
		super(parent);
		
		this.function = function;
		this.params = params;
		
		//Check for a null parameter collection
		if (params == null){
			this.params = new LinkedList<String>();
		}
	}
	
	protected FunctionCallNode(AbstractNode parent, Codes.Invoke code){
		super(parent);
		
		this.function = code.name.name();
		this.params = new LinkedList<String>(); //TODO: Work out how to parameter
	}

	@Override
	public String evaluate() {
		String val = function + "("; //open function brackets
		
		//add parameters
		for (int i = 0; i < params.size(); i++){
			val += params.get(i);
			
			//Comma between params
			if (i != params.size()-1){
				val += ",";
			}
		}
		
		val += ");\n"; //close function brackets
		return val;
	}


}
