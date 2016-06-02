package ast;

import wyil.lang.Codes;

public class UpdateNode extends AbstractNode {
	String target = null;
	String index = null;
	String operand = null;

	public UpdateNode(AbstractNode parent, Codes.Update code) {
		super(parent);
		int size = code.operands().length;
		
		this.target = VAR_PREFIX+code.target(0);
		
		//Last index is the value we update the target to
		this.operand = VAR_PREFIX+code.operand(size-1);
		
		//If there's a second operand, the first is the variable of thei index of the array to update
		//(because god forbid using the value directly)
		if (size == 2){
			index = VAR_PREFIX+code.operand(0);
		}
	}

	@Override
	public String translate() {
		String val = target;
		
		//If there's an index, reference it
		if(index != null){
			val += "["+index+"]"; 
		}
		
		val += " = "+operand+";\n";
		return val;
	}

}
