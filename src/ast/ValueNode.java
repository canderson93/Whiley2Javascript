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

	public ValueNode(AbstractNode parent, Codes.Const code){
		super(parent);

		this.target = "$"+code.target();
		this.value = code.constant.toString();
		
		parent.addVariable(this.target);
	}
	
	public ValueNode(AbstractNode parent, Codes.Assign code){
		super(parent);
		
		this.target = "$"+code.target(0);
		this.value = "$"+code.operands()[0];
		
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
