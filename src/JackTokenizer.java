import java.util.*;

public class JackTokenizer {
	
	Scanner in;
	
	public JackTokenizer(String inputFile) {
		in = new Scanner(inputFile);
	}
	
	public boolean hasMoreTokens() {
		return true;
	}
	
	public void advance() {
		
	}
	
	public String tokenType() {
		return "";
	}
	
	public String keyWord() {
		return "";
	}
	
	public char symbol() {
		return ' ';
	}
	
	public String identifier() {
		return "";
	}
	
	public int intVal() {
		return 0;
	}
	
	public String stringVal() {
		return "";
	}
	
}
