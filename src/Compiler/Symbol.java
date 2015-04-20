package Compiler;

public class Symbol {

	private String name;
	private String type;
	private String kind;
	private int runningIndex;
	
	public Symbol(String n, String t, String k, int r) {
		name = n;
		type = t;
		kind = k;
		runningIndex = r;
	}
	
	
	public void define(String n, String t, String k, int i) {
		name = n;
		type = t;
		kind = k;
		runningIndex = i;
	}
	
	public int varCount(String kind) {
		return runningIndex;
	}
	
	public String kindOf(String name) {
		return kind;
	}
	
	public String typeOf(String name) {
		return type;
	}
	
	public int indexOf(String name) {
		
	}
	
}
