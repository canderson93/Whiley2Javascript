package ast;

import wyil.lang.Codes;

public class OperatorNode extends AbstractNode {	
	private String target;
	private String operator;
	private String[] operands;
	
	public OperatorNode(AbstractNode parent, Codes.BinaryOperator code) {
		super(parent);
		
		target = "$"+code.target(0);
		operator = generateOperator(code.kind);
		operands = new String[2];
		
		operands[0] = "$"+code.operand(0);
		operands[1] = "$"+code.operand(1);
	}

	@Override
	public String evaluate() {
		String val = String.format("%s = %s %s %s;\n", target, operands[0], operator, operands[1]);
		return val;
	}
	
	private static String generateOperator(Codes.BinaryOperatorKind kind){
		switch(kind){
		case ADD:
			return "+";
		case BITWISEAND:
			return "&";
		case BITWISEOR:
			return "|";
		case BITWISEXOR:
			return "^";
		case DIV:
			return "/";
		case LEFTSHIFT:
			return "<<";
		case MUL:
			return "*";
		case REM:
			return "%";
		case RIGHTSHIFT:
			return ">>";
		case SUB:
			return "-";
		}
		
		throw new RuntimeException("Invalid operator kind");
	}

}
