package ast;

import java.util.ArrayList;
import java.util.List;

import wyil.lang.Code;
import wyil.lang.Codes;
import wyil.lang.WyilFile.FunctionOrMethod;

/**
 * Node that creates a loop
 * @author Carl
 *
 */
public class LoopNode extends AbstractNode {
	List<AbstractNode> children; //objects inside the loop
	String label; //label that is used for this node's loop

	protected LoopNode(AbstractNode parent, Codes.Loop code) {
		super(parent);
		
		children = new ArrayList<AbstractNode>();
		label = getLoopLabel();
		
		//children.add(new ValueNode(parent, LABEL_VAR, "'"+this.label+"'")); //Set the switch value to the current loop
		children.add(new LabelNode(parent, this.label)); //Mark a label for the loop
		
		for (Code c : code.bytecodes()){
			children.addAll(createNodeFromCode(c, this));
		}
		
		//Make the final node of the loop resetting the label to the loop, in case it has changed
		children.add(new ValueNode(parent, LABEL_VAR, "'"+this.label+"'"));
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
		String val = "";

		//Insert children (somewhere in here is a break statement)
		for (AbstractNode node : children){
			val += node.translate();
		}

		val += "break;\n"; //Make sure the loop doesn't break out prematurely
		return val;
	}

}
