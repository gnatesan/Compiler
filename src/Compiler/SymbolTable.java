package Compiler;

import java.util.HashMap;

public class SymbolTable {

	private HashMap<String, Symbol> test;
	private Symbol sym;
	private variableCount vc;
	
	//ienttifier starts at 0 and increments at one until
	
	
	public void startSubroutine() {
		test.clear();
	}
	
	public void define(String n, String t, variableCount vc) {
		//create a symbol object and enter into hash map
		sym = new Symbol(n, t, vc.value());
		test.put(n,sym);
	}
	
	public HashMap<String, Symbol> getTable() {
		return test;
	}
	
	private void increment(variableCount vc) {
    	vc.increment();	
    }
	
	public int VarCount(variableCount sample) {
		return sample.value();
	}
	
	public variableCount kindOf(String name) { //FIELD STATIC VAR ARGUMENT
		if (test.containsKey(name)) {
			Symbol temp = test.get(name);
			switch (temp.getType()) {
			case("static"):
				return vc.STATIC;
			case("field"):
				return vc.FIELD;
			case("argument"):
				return vc.ARGUMENT;
			case("var"):
				return vc.VAR;
			}
		}
		return vc.NONE;
	}
	
	public String typeOf(String name) {
		return test.get(name).getType();
	}
	
	public int indexOf(String name) {
		return test.get(name).getCount();
	}
	
}
