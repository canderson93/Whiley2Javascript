package ast;

import java.util.ArrayList;
import java.util.List;

import wyil.lang.Code;
import wyil.lang.Codes;
import wyil.lang.WyilFile.FunctionOrMethod;

public class LoopNode extends AbstractNode {
	List<AbstractNode> children;

	protected LoopNode(AbstractNode parent, Codes.Loop code) {
		super(parent);
		
		children = new ArrayList<AbstractNode>();

		for (Code c : code.bytecodes()){
			children.addAll(createNodeFromCode(c, this));
		}
		
		throw new RuntimeException("Loops are temporarily disabled");
	}

	protected LoopNode(AbstractNode parent, FunctionOrMethod function){
		super(parent);

		children = new ArrayList<AbstractNode>();

		for (Code c : function.body().bytecodes()){
			children.addAll(createNodeFromCode(c, this));
		}
	}

	@Override
	public String translate() {
		//Going to try use the switch statement to simulate a loop
		String val = "";

		//Insert children (somewhere in here is a break statement)
		for (AbstractNode node : children){
			val += node.translate();
		}

		val += "break;\n"; //Make sure the loop doesn't break out prematurely
		return val;
	}

}
