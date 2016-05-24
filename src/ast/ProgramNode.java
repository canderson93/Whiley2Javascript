package ast;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wyil.lang.WyilFile;
import wyil.lang.WyilFile.FunctionOrMethod;

/**
 * A Javascript AST node representing the top level program.
 * 
 * @author Carl
 *
 */
public class ProgramNode extends AbstractNode {
	private List<AbstractNode> children;

	public ProgramNode(WyilFile file) {
		super(null);

		children = new LinkedList<AbstractNode>();
		Collection<FunctionOrMethod> functions = file.functionOrMethods();

		//Generate all the program functions
		for (FunctionOrMethod func : functions) {
			AbstractNode node = new FunctionNode(this, func);
			children.add(node);
		}
	}

	@Override
	public String evaluate() {
		//First off, add in a fail function for assertions
		String val = "function fail() {\n throw new Error('Assertion failed'); } \n\n";

		for (AbstractNode node : children) {
			val += node.evaluate();
			val += "\n"; // for good measure
		}

		return val;
	}

}
