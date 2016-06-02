package ast;

import java.util.HashMap;
import java.util.Map;

import wycc.util.Pair;
import wyil.lang.Codes;
import wyil.lang.Constant;

public class SwitchNode extends AbstractNode {
	Map<String, GoToNode> branches;
	String operand;
	String def; //default case
	
	
	public SwitchNode(AbstractNode parent, Codes.Switch code) {
		super(parent);
		
		branches = new HashMap<String, GoToNode>();
		def = code.defaultTarget;
		operand = VAR_PREFIX+code.operand(0);
		
		for (Pair<Constant, String> p : code.branches){
			branches.put(p.first().toString(), new GoToNode(this, p.second()));
		}
	}

	@Override
	public String translate() {
		//ToString conversions to subvert array comparisons
		String val = String.format("switch(%s.toString()) {\n", operand);
		
		for (String c : branches.keySet()){
			val += String.format("case (%s).toString():\n", c);
			val += branches.get(c).translate();
		}
		
		//Add in the default case
		val += "default:\n";
		val += new GoToNode(this, def).translate();
		
		//Close the switch
		val += "}\nbreak;\n";
		
		return val;
	}

}
