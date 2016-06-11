package ast;

/**
 * Node for fail statements with assertions
 * @author Carl
 *
 */
public class FailNode extends AbstractNode {

	protected FailNode(AbstractNode parent) {
		super(parent);
	}

	@Override
	public String translate() {
		return "throw new Error('Assertion Failed');\n";
	}

}
