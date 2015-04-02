import java.io.*;
import java.util.*;

public class JackTokenizer {
	
	private Scanner in;
	private String outFile;
	private String wholeFile = "";
	private String type = "DNE";
	private String next;
	private String nextToken;
	private StringTokenizer st;
	
	public JackTokenizer(String inputFile) {
		outFile = inputFile.replace(".jack", ".xml");
		try {
			File f = new File(inputFile);
			in = new Scanner(f);
			FileWriter fw = new FileWriter(outFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(in.hasNextLine() && in.hasNext()){
			next = in.nextLine();
			if (next.contains("//")) {
				next = next.substring(0, next.indexOf('/'));
			}
			wholeFile += next;
		}	
		//wholeFile = wholeFile.replaceAll(" ", "");
		while (wholeFile.contains("/**") && wholeFile.contains("*/")) {
			wholeFile = wholeFile.substring(0, wholeFile.indexOf("/**")) + 
						wholeFile.substring(wholeFile.indexOf("*/") + 2  , wholeFile.length());
		}
		st = new StringTokenizer(wholeFile);
	}
	
	public boolean hasMoreTokens() {
		if (st.hasMoreTokens())
			return true;
		return false;
	}
	
	public void advance() {
		nextToken = st.nextToken();
		type = tokenType();
		
		
	}
	
	public String tokenType() {
			
		try {
			int i = Integer.parseInt(nextToken);
			type = "INT_CONST";
		}
		catch(NumberFormatException e) {
			switch (nextToken){
			// case checks if it is a KEYWORD
					case "class":
						type = "KEYWORD";
						break;
					case "constructor":
						type = "KEYWORD";
						break;
					case "function":
						type = "KEYWORD";
						break;
					case "method":
						type = "KEYWORD";
						break;
					case "field":
						type = "KEYWORD";
						break;
					case "static":
						type = "KEYWORD";
						break;
					case "var":
						type = "KEYWORD";
						break;
					case "int":
						type = "KEYWORD";
						break;
					case "char":
						type = "KEYWORD";
						break;
					case "boolean":
						type = "KEYWORD";
						break;
					case "void":
						type = "KEYWORD";
						break;
					case "true":
						type = "KEYWORD";
						break;
					case "false":
						type = "KEYWORD";
						break;
					case "null":
						type = "KEYWORD";
						break;
					case "this":
						type = "KEYWORD";
						break;
					case "let":
						type = "KEYWORD";
						break;
					case "do":
						type = "KEYWORD";
						break;
					case "if":
						type = "KEYWORD";
						break;
					case "else":
						type = "KEYWORD";
						break;
					case "while":
						type = "KEYWORD";
						break;
					case "return":
						type = "KEYWORD";
						break;
						
						
			// Cases to check if type is SYMBOL
					case "{":
						type = "SYMBOL";
						break;
					case "}":
						type = "SYMBOL";
						break;
					case "(":
						type = "SYMBOL";
						break;
					case ")":
						type = "SYMBOL";
						break;
					case "[":
						type = "SYMBOL";
						break;
					case "]":
						type = "SYMBOL";
						break;
					case ".":
						type = "SYMBOL";
						break;
					case ",":
						type = "SYMBOL";
						break;
					case ";":
						type = "SYMBOL";
						break;
					case "+":
						type = "SYMBOL";
						break;
					case "-":
						type = "SYMBOL";
						break;
					case "*":
						type = "SYMBOL";
						break;
					case "/":
						type = "SYMBOL";
						break;
					case "&":
						type = "SYMBOL";
						break;
					case "|":
						type = "SYMBOL";
						break;
					case "<":
						type = "SYMBOL";
						break;
					case ">":
						type = "SYMBOL";
						break;
					case "=":
						type = "SYMBOL";
						break;
					case "_":
						type = "SYMBOL";
						break;
					}
			
			if(!nextToken.startsWith("\"") && !nextToken.endsWith("\"") && !nextToken.contains("/n")) {
				type = "STRING_CONST";
			}
			else {
				type = "IDENTIFIER";
			}
			
		}
		return type;
	}
	
	public String keyword() {
		//String identiferLeft = "<keyword> ";
		//String identiferRight = " </keyword>";
		
		//return identiferLeft + nextToken + identiferRight;
		
		return nextToken;
	}
	
	public char symbol() {
		/*String identiferLeft = "<identifier> ";
		String identiferRight = " </identifier>";
		return identiferLeft + nextToken + identiferRight;*/
		
		
	return nextToken.charAt(0);
	}
	
	public String identifier() {
		return nextToken;
	}
	
	public int intVal() {
		return Integer.parseInt(nextToken);
	}
	
	public String stringVal() {
		nextToken = nextToken.replaceAll("\"", "");
		nextToken = nextToken.replaceAll("\n", "");
		return nextToken;
	}
	
	/*public static void main (String[] args) {
		try {
			JackTokenizer k = new JackTokenizer("test.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
