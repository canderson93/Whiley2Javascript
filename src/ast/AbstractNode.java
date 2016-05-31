package ast;

import java.util.ArrayList;
import java.util.List;

import wyil.lang.Code;
import wyil.lang.Codes;

public abstract class AbstractNode {
	public static final String LABEL_VAR = "$label";
	
	protected AbstractNode parent;
	protected AbstractNode next;
	
	protected AbstractNode(AbstractNode parent){
		this.parent = parent;
		
		if (parent != null){
			parent.next = this;
		}
	}
	
	/**
	 * Produce a javascript representation of this node
	 * @return
	 */
	public abstract String translate();

	/**
	 * Adds a label to the current scope
	 * 
	 * @param label label identifier
	 * @param next node which begins after the label
	 */
	protected void addLabel(String label, LabelNode node){
		//Implement here, as most cases just want to propagate
		//the value up the chain until it reaches a node who cares
		if (parent != null){
			parent.addLabel(label, node);
		}
	};
	
	/**
	 * Registers a variable for the current scope
	 * @param var the variable name to register
	 */
	protected void addVariable(String var){
		if (parent != null){
			parent.addVariable(var);
		}
	}
	
	/**
	 * Creates and returns a list of nodes that represent the Code object
	 * @param code
	 * @param parent
	 */
	public static List<AbstractNode> createNodeFromCode(Code code, AbstractNode parent){
		List<AbstractNode> list = new ArrayList<AbstractNode>();
		
		//Assert Or Assume
		if (code instanceof Codes.AssertOrAssume){
			Codes.AssertOrAssume c = (Codes.AssertOrAssume)code;
			list.addAll(createAssertBlock(c, parent));
		}
		//Constants
		else if (code instanceof Codes.Const){
			Codes.Const c = (Codes.Const)code;
			list.add(new ValueNode(parent, c));
		}
		//Assignments
		else if (code instanceof Codes.Assign){
			Codes.Assign c = (Codes.Assign)code;
			list.add(new ValueNode(parent, c));
		}
		//Operator Assignments
		else if (code instanceof Codes.BinaryOperator){
			Codes.BinaryOperator c = (Codes.BinaryOperator)code;
			list.add(new OperatorNode(parent, c));
		}
		//Function call
		else if (code instanceof Codes.Invoke){
			Codes.Invoke c = (Codes.Invoke)code;
			list.add(new FunctionCallNode(parent, c));
		}
		//Fail
		else if (code instanceof Codes.Fail){
			list.add(new FunctionCallNode(parent, "fail", null));
		}
		else if (code instanceof Codes.Goto){
			Codes.Goto c = (Codes.Goto)code;
			list.add(new GoToNode(parent, c));
		}
		//Label
		else if (code instanceof Codes.Label){
			Codes.Label c = (Codes.Label)code;
			list.add(new LabelNode(parent, c.label));
		}
		//If
		else if (code instanceof Codes.If){
			Codes.If c = (Codes.If)code;
			list.add(new IfNode(parent, c));
		}
		//Return
		else if (code instanceof Codes.Return){
			Codes.Return c = (Codes.Return)code;
			list.add(new ReturnNode(parent, c));
		}
		else {
			//System.out.println(code.opcode() + " " + code.getClass().getName());
			throw new RuntimeException(code.getClass().getName() + " is unimplemented");
		}

		return list;
	}
	
	private static List<AbstractNode> createAssertBlock(Codes.AssertOrAssume code, AbstractNode parent){
		if (!(code instanceof Codes.AssertOrAssume)){
			throw new IllegalArgumentException();
		}
		
		Codes.AssertOrAssume block = (Codes.AssertOrAssume)code;
		
		List<AbstractNode> list = new ArrayList<AbstractNode>();
		
		//Loop through all the children, and handle the codes appropriately
		for (Code c : block.bytecodes()){
			list.addAll(createNodeFromCode(c, parent));
		}
		
		return list;
	}
}
