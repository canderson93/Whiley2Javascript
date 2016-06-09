package ast;

import java.util.ArrayList;
import java.util.List;

import wyil.lang.Codes;
import wyil.lang.Type;

/**
 * A node to represent a simple value assignment.
 * @author Carl
 *
 */
public class ValueNode extends AbstractNode {
	private String target;
	private List<String> value;
	
	private boolean isArray = false;
	
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
		
		String val = VAR_PREFIX+code.operand(0);
		
		//Special handling for Integers: Wrap in a Math.floor to simulate truncation
		if (code.type(0) instanceof Type.Int){
			val = "Math.floor("+val+")";
		}

		//As far as I'm aware, these only ever have one value
		this.target = VAR_PREFIX+code.target(0);
		this.value.add(val);

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
		
		isArray = true;
		parent.addVariable(this.target);
	}

	public ValueNode(AbstractNode parent, Codes.ArrayGenerator code){
		super(parent);

		this.value = new ArrayList<String>();
		this.target = VAR_PREFIX+code.target(0);
		
		//If this way passed in some operators, then we need to initalize the array
		//with a length
		if (code.operands().length != 0){
			String val = VAR_PREFIX+code.operand(0);
			String len = VAR_PREFIX+code.operand(1);
			
			//Inject a function to fill the array with the desired values
			value.add(String.format("Array.apply(null, Array(%s)).map(function(){ return %s; })", len, val));
			
		} else {
			isArray = true;
		}
		
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
		if (isArray){
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
		
		//if this isn't an array, just return the value (should only be one)
		return value.get(0);
	}

}
