package ast;

import wyil.lang.Codes;
import wyil.lang.Type;

/**
 * A node to represent if statements
 * @author Carl
 *
 */
public class IfNode extends AbstractNode {
	private String condition; //the condition to evaluate
	private String target; //goto label to go to if the condition is true
	
	public IfNode(AbstractNode parent, Codes.If code) {
		super(parent);

		target = code.target;
		condition = generateCondition(code, code.type(0) instanceof Type.Array);
	}

	@Override
	public String translate() {
		String val = String.format("if (%s){\n", condition);
		val += String.format("%s = '%s';\n", LABEL_VAR, target);
		val += "break; \n }\n";

		return val;
	}

	private static String generateCondition(Codes.If code, boolean isArray){
		String condition = "$"+code.operand(0)+""; //Left hand
		
		//String conversion so array comparisons don't just compare the references
		if (isArray){
			condition += ".toString()";
		}

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

		if (isArray){
			condition += ".toString()";
		}
		
		return condition;
	}

}
