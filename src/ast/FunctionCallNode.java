package ast;

import java.util.ArrayList;
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
	private String target;

	protected FunctionCallNode(AbstractNode parent) {
		super(parent);

		params = new LinkedList<String>();
	}

	protected FunctionCallNode(AbstractNode parent, String function, List<String> params, String target){
		super(parent);

		this.function = function;
		this.params = params;

		//Check for a null parameter collection
		if (params == null){
			this.params = new LinkedList<String>();
		}

		target = this.target;
	}

	protected FunctionCallNode(AbstractNode parent, String function, List<String> params){
		this(parent, function, params, null);
	}

	protected FunctionCallNode(AbstractNode parent, Codes.Invoke code){
		super(parent);

		this.function = code.name.name();
		this.params = loadParams(code); //TODO: Work out how to parameter

		//Input any target
		if (code.targets().length > 0){
			target = "$"+code.target(0);
			parent.addVariable(target);
		} else {
			target = null;
		}
	}

	@Override
	public String translate() {
		String val = "";

		//Put in any target assignment
		if (target != null){
			val += String.format("%s = ", target);
		}

		//Open the function
		val += function + "(";

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

	private List<String> loadParams(Codes.Invoke code){
		List<String> params = new ArrayList<String>();
		int[] ops = code.operands();

		for (int op : ops){
			params.add(String.format("$%d", op));
		}

		return params;
	}
}
