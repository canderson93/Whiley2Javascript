package ast;

import wyil.lang.Codes;

/**
 * Node to call a property of an object
 * @author anderscarl1
 *
 */
public class PropertyAccessNode extends AbstractNode {
	String target;
	String operand;
	String property;

	/**
	 * Generic constructor for direct assignment
	 * @param parent Parent in the AST
	 * @param target Target to assign the value to
	 * @param operand Object we want to access the property of
	 * @param property The name of the property
	 */
	protected PropertyAccessNode(AbstractNode parent, String target, String operand, String property){
		super(parent);

		this.operand = VAR_PREFIX+operand;
		this.target = VAR_PREFIX+target;
		this.property = property;

		parent.addVariable(this.target);
	}

	/**
	 * Constructor to access the length property
	 * @param parent Parent in the AST
	 * @param code Code to generate the node from
	 */
	protected PropertyAccessNode(AbstractNode parent, Codes.LengthOf code) {
		this(parent, Integer.toString(code.target(0)), Integer.toString(code.operand(0)), "length");
	}

	@Override
	public String translate() {
		String val = String.format("%s = %s.%s;\n", target, operand, property);
		return val;
	}

}
