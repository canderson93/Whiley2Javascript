package ast;

/**
 * A node to handle WyIL labels for use with gotos
 * @author Carl
 *
 */
public class LabelNode extends AbstractNode {
	public final String label;
	
	public LabelNode(AbstractNode parent, String label) {
		super(parent);
		
		this.label = label;
		parent.addLabel(label, this);
	}

	@Override
	//End the current parsing line
	public String evaluate() {
		String val = String.format("case '%s':\n", label);
		//TODO: May need to place a break statement in here
		return val;
	}

}
