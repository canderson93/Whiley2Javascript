package ast;

import java.util.ArrayList;
import java.util.List;

import wyil.lang.Codes;

public class ArrayReferenceNode extends AbstractNode {
	String target;
	String array;
	String index;

	List<String> operands;

	protected ArrayReferenceNode(AbstractNode parent, Codes.IndexOf code) {
		super(parent);

		this.target = VAR_PREFIX+code.target(0);
		this.operands = new ArrayList<String>();

		this.array = VAR_PREFIX+code.operand(0);
		this.index = VAR_PREFIX+code.operand(1);
	}

	@Override
	public String translate() {
		String val = String.format("%s = %s[%s];\n", target, array, index);
		return val;
	}

}
