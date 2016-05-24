package ast;

import wyil.lang.Code;
import wyil.lang.Codes;

/**
 * A node to represent a simple value assignment. 
 * @author Carl
 *
 */
public class ValueNode extends AbstractNode {
	private String target;
	private String value;

	public ValueNode(AbstractNode parent, Code code){
		super(parent);

		//TODO: Check this isn't done any other way
		Codes.Const val = (Codes.Const)code;
		this.target = "$"+val.target();
		this.value = val.constant.toString();
		
		parent.addVariable(this.target);
	}
	
	public ValueNode(AbstractNode parent, String value) {
		super(parent);
		
		this.value = value;
	}

	@Override
	public String evaluate() {
		String val = String.format("%s = %s;\n", target, value);
		
		return val;
	}

}
