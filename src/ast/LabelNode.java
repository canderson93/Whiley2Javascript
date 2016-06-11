package ast;

/**
 * A node to handle WyIL labels for use with gotos
 * @author Carl
 *
 */
public class LabelNode extends AbstractNode {
	public final String label; //label declaration
	
	public LabelNode(AbstractNode parent, String label) {
		super(parent);
		
		this.label = label;
	}

	@Override
	//End the current parsing line
	public String translate() {
		String val = String.format("case '%s':\n", label);
		return val;
	}

}
