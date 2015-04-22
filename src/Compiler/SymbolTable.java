package Compiler;

import java.util.HashMap;

public class SymbolTable {

	private String name;
	private String type;
	private String kind;
	private int runningIndex;
	private HashMap<String, Symbol> st;
	private Symbol sym;
	
	
	/*Define that create a sybmbol oject
	 * kind passed in from comilation engine
	 */
	
	
	
	//public enum Segment {
	//	STATIC("static"), ARGUMENT ("argument"), LOCAL ("local"), THIS("this"), 
	//	THAT ("that") , CONSTANT("constant"), POINTER("pointer"), TEMP("temp");
		
	//	private String segmentValue;
		
//
	//	Segment(String aValue) {
	//		segmentValue = aValue;
	//	}
		
	//	public String toString() {
		//	return segmentValue;
		//}
//	}
	
	public void startSubroutine() {
		st.clear();
	}
	
	public void define(String n, String t, String k, int i) {
		//create a symbol object and enter into hash map
		if(name.)
		sym = new Symbol(n, t, k, i, incrementCorrespondingInteger(t), getCorrespondingKindInteger(k));
		st.put(n,sym);
		
		
	}
	
	
	
}
