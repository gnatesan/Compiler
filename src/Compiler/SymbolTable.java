package Compiler;

import java.util.HashMap;

public class SymbolTable {

	public HashMap<String, Symbol> test;
	
	
	//ienttifier starts at 0 and increments at one until
	
	
	public void startSubroutine() {
		test.clear();
	}
	
	public void define(String n, String t, String k, int counter) {
		Symbol s = new Symbol(n, t, k, counter);
		test.put(s.name, s);
	}
	
	public String kindOf(String name) {
		return test.get(name).kind;
	}
	
	public String typeOf(String name) {
		return test.get(name).type;
	}
	
	public int indexOf(String name) {
		return test.get(name).count;
	}

	public HashMap<String, Symbol> getTest() {
		return test;
	}
	
	
	
}
