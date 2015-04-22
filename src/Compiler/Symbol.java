package Compiler;

public class Symbol {

	public String name;
	public String type;
	public String kind;
	public int count;
	
	public Symbol(String n, String t, String k, int counter) {
		name = n;
		type = t;
		if (k.equals("field"))
			kind = "this";
		else if (k.equals("var"))
			kind = "local";
		else 
			kind = k;
		count = counter;
	}
	
}
