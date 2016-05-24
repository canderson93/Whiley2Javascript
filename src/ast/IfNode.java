package ast;

import wyil.lang.Codes;

/**
 * A node to represent if statements
 * @author Carl
 *
 */
public class IfNode extends AbstractNode {
	private String condition;
	private String target;

	public IfNode(AbstractNode parent, Codes.If code) {
		super(parent);
		
		target = code.target;
		condition = generateCondition(code);
	}

	@Override
	public String evaluate() {
		String val = String.format("if (%s){\n", condition);
		val += String.format("%s = '%s';\n", LABEL_VAR, target);
		val += "continue; \n }\n"; //TODO: Break or continue here?

		return val;
	}
	
	private static String generateCondition(Codes.If code){
		String condition = "$"+code.operand(0); //Left hand
		
		//Insert the comparator
		switch (code.op){
		case EQ:
			condition += " == ";
			break;
		case GT:
			condition += " > ";
			break;
		case GTEQ:
			condition += " >= ";
			break;
		case LT:
			condition += " < ";
			break;
		case LTEQ:
			condition += " <= ";
			break;
		case NEQ:
			condition += " != ";
			break;
		}
		
		condition += "$"+code.operand(1); //Right hand
		
		return condition;
	}

}
