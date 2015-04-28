package Compiler;

import java.util.HashMap;

public class SymbolTable {

	public HashMap<String, Symbol> global;
	public HashMap<String, Symbol> local;
	public int fieldCount;
	public int staticCount;
	public int varCount;
	public int argCount;
	
	
	public SymbolTable() {
		global = new HashMap<String, Symbol>();
		local = new HashMap<String, Symbol>();
	}
	
	
	public void startSubroutine() {
		local.clear();
		varCount = 0;
		argCount = 0;
	}
	
	public void define(String n, String t, String k) {
		Symbol s;
		switch(k) {
		case "static":
			s = new Symbol(n, t, k, staticCount);
			staticCount += 1;
			global.put(s.name, s);
			break;
		case "field":
			s = new Symbol(n, t, k, fieldCount);
			fieldCount += 1;
			global.put(s.name, s);
			break;
		case "var":
			s = new Symbol(n, t, k, varCount);
			varCount += 1;
			local.put(s.name, s);
			break;
		case "argument":
			s = new Symbol(n, t, k, argCount);
			argCount += 1;
			local.put(s.name, s);
			break;
		}
	}
	
	public String kindOf(String name) {
		if (local.containsKey(name)) {
			return local.get(name).kind;
		}
		else if (global.containsKey(name)) {
			return global.get(name).kind;
		}
		else return "null";
		
	}
	
	public String typeOf(String name) {
		if (local.containsKey(name)) {
			return local.get(name).type;
		}
		else if (global.containsKey(name)) {
			return global.get(name).type;
		}
		else return "null"; 
	}
	
	public int indexOf(String name) {
		if (local.containsKey(name)) {
			return local.get(name).count;
		}
		else if (global.containsKey(name)) {
			return global.get(name).count;
		}
		else return -1;
	}
}
