package Compiler;

import java.io.*;
import java.util.*;

public class JackTokenizer {
		
		private Scanner in;
		private String wholeFile = "";
		private String next;
		private String nextToken;
		int masterCount = 0;
		private StringTokenizer st;
		String[] allTokens;
		int var = 0;
		private HashMap<String, String> symbolXML;
		private tokenType token;


		private keyWord key;
		public enum tokenType {
			KEYWORD ("keyword"), SYMBOL ("symbol"), STRING_CONST ("stringConstant"), IDENTIFER("identifier"), 
			INT_CONST ("integerConstant");
			
			private String value;
		
			tokenType(String aValue) {
				value = aValue;
			}
			
			public String toString() {
				return value;
			}
		}
		
		enum keyWord {CLASS, METHOD, FUNCTION, CONSTRUCTOR, INT, BOOLEAN, CHAR, VOID, VAR, STATIC, FIELD, LET, DO, IF, 
			ELSE, WHILE, RETURN, TRUE, FALSE, NULL, THIS}
		
		
		
		
		
		public JackTokenizer(String inputFile) {
			//outFile = inputFile.replace(".jack", "T.xml");
			try {
				File f = new File(inputFile);
				in = new Scanner(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			symbolXML = new HashMap<String, String>();
			symbolXML.put("<", "&lt;");
			symbolXML.put(">", "&gt;");
			symbolXML.put("&", "&amp;");
			while(in.hasNextLine() && in.hasNext()){
				next = in.nextLine();
				if (next.contains("//")) {
					next = next.substring(0, next.indexOf('/'));
				}
				wholeFile += next;
			}	
			
			//tokenType KEYWORD = new tokenType("test");
			//wholeFile = wholeFile.replaceAll(" ", "");
			//Remove comments from code
			while (wholeFile.contains("/**") && wholeFile.contains("*/")) {
				wholeFile = wholeFile.substring(0, wholeFile.indexOf("/**")) + 
							wholeFile.substring(wholeFile.indexOf("*/") + 2  , wholeFile.length());
			}
			if (wholeFile.contains("(")) {
				wholeFile = wholeFile.replace("(", " ( ");
			}
			if (wholeFile.contains(")")) {
				wholeFile = wholeFile.replace(")", " ) ");
			}
			if (wholeFile.contains(".")) {
				wholeFile = wholeFile.replace(".", " . ");
			}
			if (wholeFile.contains(";")) {
				wholeFile = wholeFile.replace(";", " ; ");
			}
			if (wholeFile.contains("{")) {
				wholeFile = wholeFile.replace("{", " { ");
			}
			if (wholeFile.contains("}")) {
				wholeFile = wholeFile.replace("}", " } ");
			}
			if (wholeFile.contains("[")) {
				wholeFile = wholeFile.replace("[", " [ ");
			}
			if (wholeFile.contains("]")) {
				wholeFile = wholeFile.replace("]", " ] ");
			}
			if (wholeFile.contains(",")) {
				wholeFile = wholeFile.replace(",", " , ");
			}
			if (wholeFile.contains("\"")) {
				wholeFile = wholeFile.replace("\"", " \" ");
			}
			if (wholeFile.contains("-")) {
				wholeFile = wholeFile.replace("-", " - ");
			}
			if (wholeFile.contains("~")) {
				wholeFile = wholeFile.replace("~", " ~ ");
			}
			allTokens = wholeFile.split("\\s+");
			//for (String s : allTokens)
				//System.out.println(s);
			//st = new StringTokenizer(wholeFile);
		}
		
		
		// Getter for nextToken
		public String getNextToken() {
			if (symbolXML.containsKey(nextToken)) {
				return symbolXML.get(nextToken);
			}
			return nextToken;
		}
		public tokenType getToken() {
			return token;
		}
		
		public boolean hasMoreTokens() {
			if (masterCount < allTokens.length)
				return true;
			return false;
		}
		
		public void advance() {	//"HOW MANY NUMBERS?  " -> " HOW MANY NUMBERS ? " 
			if (allTokens[masterCount].equals("\"")) {
				this.nextToken = allTokens[masterCount];
				masterCount += 1;
				while (!allTokens[masterCount].equals("\"")) {
					this.nextToken += allTokens[masterCount];
					masterCount += 1;
				}
				this.nextToken += allTokens[masterCount];
				masterCount += 1;
			}
			else {
				this.nextToken = allTokens[masterCount];
				masterCount += 1;
			}
		}
		
		public tokenType tokenType() {
			token = tokenType.IDENTIFER;
			try {
				int i = Integer.parseInt(nextToken);
				token = tokenType.INT_CONST;
				return token;
			}
			catch(NumberFormatException e) {
				switch (nextToken){
				// case checks if it is a Tokentype.KEYWORD
						case "class":
							token = tokenType.KEYWORD;
							break;
						case "constructor":
							token = tokenType.KEYWORD;
							break;
						case "function":
							token = tokenType.KEYWORD;
							break;
						case "method":
							token = tokenType.KEYWORD;
							break;
						case "field":
							token = tokenType.KEYWORD;
							break;
						case "static":
							token = tokenType.KEYWORD;
							break;
						case "var":
							token = tokenType.KEYWORD;
							break;
						case "int":
							token = tokenType.KEYWORD;
							break;
						case "char":
							token = tokenType.KEYWORD;
							break;
						case "boolean":
							token = tokenType.KEYWORD;
							break;
						case "void":
							token = tokenType.KEYWORD;
							break;
						case "true":
							token = tokenType.KEYWORD;
							break;
						case "false":
							token = tokenType.KEYWORD;
							break;
						case "null":
							token = tokenType.KEYWORD;
							break;
						case "this":
							token = tokenType.KEYWORD;
							break;
						case "let":
							token = tokenType.KEYWORD;
							break;
						case "do":
							token = tokenType.KEYWORD;
							break;
						case "if":
							token = tokenType.KEYWORD;
							break;
						case "else":
							token = tokenType.KEYWORD;
							break;
						case "while":
							token = tokenType.KEYWORD;
							break;
						case "return":
							token = tokenType.KEYWORD;
							break;
							
							
				// Cases to check if type is SYMBOL
						case "{":
							token = tokenType.SYMBOL;
							break;
						case "}":
							token = tokenType.SYMBOL;
							break;
						case "(":
							token = tokenType.SYMBOL;
							break;
						case ")":
							token = tokenType.SYMBOL;
							break;
						case "[":
							token = tokenType.SYMBOL;
							break;
						case "]":
							token = tokenType.SYMBOL;
							break;
						case ".":
							token = tokenType.SYMBOL;
							break;
						case ",":
							token = tokenType.SYMBOL;
							break;
						case ";":
							token = tokenType.SYMBOL;
							break;
						case "+":
							token = tokenType.SYMBOL;
							break;
						case "-":
							token = tokenType.SYMBOL;
							break;
						case "*":
							token = tokenType.SYMBOL;
							break;
						case "/":
							token = tokenType.SYMBOL;
							break;
						case "&":
							token = tokenType.SYMBOL;
							break;
						case "|":
							token = tokenType.SYMBOL;
							break;
						case "<":
							token = tokenType.SYMBOL;
							break;
						case ">":
							token = tokenType.SYMBOL;
							break;
						case "=":
							token = tokenType.SYMBOL;
							break;
						case "_":
							token = tokenType.SYMBOL;
							break;
						case "~":
							token = tokenType.SYMBOL;
							break;
						}

				if (token == tokenType.SYMBOL || token == tokenType.KEYWORD) {
					return token;
				}
				else if(nextToken.startsWith("\"") && nextToken.endsWith("\"") && !nextToken.contains("/n")) {
					token = tokenType.STRING_CONST;
				}
				else {
					token = tokenType.IDENTIFER;
				}		
			}
			return token;
		}
		
		public keyWord keyWord(String nextToken) {
			switch (nextToken){	
			case "class":
				key = keyWord.CLASS;
				break;
			case "constructor":
				key = keyWord.CONSTRUCTOR;
				break;
			case "function":
				key = keyWord.FUNCTION;
				break;
			case "method":
				key = keyWord.METHOD;
				break;
			case "field":
				key = keyWord.FIELD;
				break;
			case "static":
				key = keyWord.STATIC;
				break;
			case "var":
				key = keyWord.VAR;
				break;
			case "int":
				key = keyWord.INT;
				break;
			case "char":
				key = keyWord.CHAR;
				break;
			case "boolean":
				key = keyWord.BOOLEAN;;
				break;
			case "void":
				key = keyWord.VOID;
				break;
			case "true":
				key = keyWord.TRUE;
				break;
			case "false":
				key = keyWord.FALSE;
				break;
			case "null":
				key = keyWord.NULL;
				break;
			case "this":
				key = keyWord.THIS;
				break;
			case "let":
				key = keyWord.LET;
				break;
			case "do":
				key = keyWord.DO;
				break;
			case "if":
				key = keyWord.IF;
				break;
			case "else":
				key = keyWord.ELSE;
				break;
			case "while":
				key = keyWord.WHILE;
				break;
			case "return":
				key = keyWord.RETURN;
				break;
		}
			
			return key;
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
			String remove = nextToken.replaceAll("\"", "");
			remove = remove.replaceAll("\n", "");
			return remove;
			//return nextToken.replaceAll("\"",  "");
		}
		
		//retrieves value of next token without actually going to the next token
		public String peek() {
			if (masterCount < allTokens.length)
					return allTokens[masterCount];
			return null;
		}
		
		/*
		public void writeOutput() throws IOException {
			fw.write("<tokens>");
			fw.write(System.lineSeparator());
			while (this.hasMoreTokens()) {
				advance();
				fw.write("<" + tokenType().toString() + "> " + nextToken + " </" + tokenType().toString() + ">");
				fw.write(System.lineSeparator());
			}
			fw.write("</tokens>");
			fw.write(System.lineSeparator());
			fw.close();
		}
		*/
		public static void main (String[] args) {
			try {
				JackTokenizer k = new JackTokenizer("Square.jack");
				//k.writeOutput();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}
