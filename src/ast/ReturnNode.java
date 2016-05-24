package ast;

import wyil.lang.Codes;

public class ReturnNode extends AbstractNode {
	String value;

	public ReturnNode(AbstractNode parent, Codes.Return code) {
		super(parent);
		
		//value = "$"+code.target(0);
	}

	@Override
	public String evaluate() {
		return "return;\n";
	}

}
