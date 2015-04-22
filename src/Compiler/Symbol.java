package Compiler;

public class Symbol {

	private String name = "";
	private String type = "";
	private int count;
	
	public Symbol(String n, String t, int value) {
		this.name = name;
		this.type = type;
		this.count = value;
	}

	public String name(){
		return name;
	}
	
	
	public String getType() {
		return this.type;
	}
	
	public int getCount() {
		return this.count;
	}
	
	
}
