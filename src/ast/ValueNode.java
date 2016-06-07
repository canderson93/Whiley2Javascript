package ast;

import java.util.ArrayList;
import java.util.List;

import wyil.lang.Codes;

/**
 * A node to represent a simple value assignment.
 * @author Carl
 *
 */
public class ValueNode extends AbstractNode {
	private String target;
	private List<String> value;
	
	protected ValueNode (AbstractNode parent, String target, String... values){
		super(parent);
		
		this.target = target;
		this.value = new ArrayList<String>();
		
		for (String v : values){
			this.value.add(v);
		}
		
		parent.addVariable(this.target);
	}

	public ValueNode(AbstractNode parent, Codes.Const code){
		super(parent);

		this.value = new ArrayList<String>();

		this.target = VAR_PREFIX+code.target();
		this.value.add(code.constant.toString());

		parent.addVariable(this.target);
	}

	public ValueNode(AbstractNode parent, Codes.Assign code){
		super(parent);

		this.value = new ArrayList<String>();

		//As far as I'm aware, these only ever have one value
		this.target = VAR_PREFIX+code.target(0);
		this.value.add(VAR_PREFIX+code.operand(0));

		parent.addVariable(this.target);
	}

	public ValueNode(AbstractNode parent, Codes.NewArray code) {
		super(parent);

		this.value = new ArrayList<String>();
		this.target = VAR_PREFIX+code.target(0);

		//Add in the values
		for (int op : code.operands()){
			this.value.add(VAR_PREFIX+op);
		}

		parent.addVariable(this.target);
	}

	public ValueNode(AbstractNode parent, Codes.ArrayGenerator code){
		super(parent);

		this.value = new ArrayList<String>(); //Empty values array
		this.target = VAR_PREFIX+code.target(0);

		parent.addVariable(this.target);
	}
	
	public ValueNode(AbstractNode parent, Codes.UnaryOperator code){
		super(parent);
		
		this.value = new ArrayList<String>();
		this.target = VAR_PREFIX+code.target(0);

		//Assuming that all UnaryOperators are negations
		value.add("-"+VAR_PREFIX+code.operand(0));
		
		parent.addVariable(this.target);
	}

	@Override
	public String translate() {
		String values = translateValues();
		String val = String.format("%s = %s;\n", target, values);

		return val;
	}

	/**
	 * Returns an appropriate representation of values for javascript
	 * @return
	 */
	private String translateValues(){
		//if there's only one value, return the value
		if (value.size() == 1){
			return value.get(0);
		}

		//wrap the values in array notation
		String val = "[";

		//Insert values
		for (int i = 0; i < value.size(); i++){
			val += value.get(i);

			//Add a comma if this is not the last value
			if (i != value.size() -1 ){
				val += ",";
			}
		}

		val += "]";
		return val;
	}

}
