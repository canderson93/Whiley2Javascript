package ast;

import wyil.lang.Codes;

public class ReturnNode extends AbstractNode {
	String value;

	public ReturnNode(AbstractNode parent, Codes.Return code) {
		super(parent);

		//Put in the return value, if it exists
		if (code.operands().length > 0){
			value = "$"+code.operand(0);
		} else {
			value = null;
		}
	}

	@Override
	public String translate() {
		if (value == null){
			return "return;\n";
		}

		return String.format("return %s;\n",value);
	}

}
