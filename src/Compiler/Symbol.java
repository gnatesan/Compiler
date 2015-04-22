package Compiler;

public class Symbol {

	private String name = "";
	private String type = "";
	private String kind = "";
	private int identiferIndex = 0;
	private int kindIndex = 0;
	
	
	public Symbol(String name, String type, String kind, int identiferIndex, int kindIndex) {
		this.name = name;
		this.type = type;
		this.kind = kind;
		this.identiferIndex = identiferIndex;
		this.kindIndex = kindIndex;
		
	}
	
	public String name(){
		return name;
	}
	
	public int identiferIndex() {
		return identiferIndex;
	}
	
	public String kindOf() {
		return kind;
	}
	
	public String typeOf() {
		return type;
	}
	
	public int kindIndex() {
		return kindIndex;
	}
	
}
