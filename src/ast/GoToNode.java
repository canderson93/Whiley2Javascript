package ast;

import wyil.lang.Codes;

/**
 * Simulates WyIL's goto function
 * @author Carl
 *
 */
public class GoToNode extends AbstractNode {
	String target;

	public GoToNode(AbstractNode parent, Codes.Goto code) {
		super(parent);
		
		target = code.target;
	}

	@Override
	public String evaluate() {
		String val = String.format("%s = '%s';\nbreak;\n", LABEL_VAR, target);
		return val;
	}

}
