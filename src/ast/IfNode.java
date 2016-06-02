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
	public String translate() {
		String val = String.format("if (%s){\n", condition);
		val += String.format("%s = '%s';\n", LABEL_VAR, target);
		val += "break; \n }\n";

		return val;
	}

	private static String generateCondition(Codes.If code){
		//String conversion to subvert pointer comparisons for arrays etc.
		//It doesn't have a huge impact otherwise, due to implicit conversion in JavaScript
		String condition = "$"+code.operand(0)+".toString()"; //Left hand

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

		condition += "$"+code.operand(1)+".toString()"; //Right hand

		return condition;
	}

}
