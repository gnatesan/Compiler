package Compiler;

import java.io.*;
import java.util.*;

import Compiler.JackTokenizer.tokenType;

// VM translations in chapter 9

public class CompilationEngine {
	
	
	Scanner in;
	FileReader fr;
	
	private String line;
	private FileWriter fw;
	private JackTokenizer test;
	private String className;
	private String subroutineName;
	private HashMap<String, HashMap<String, HashMap<String, Integer>>> globalSymbolTable;
	private HashMap<String, HashMap<String, HashMap<String, Integer>>> localSymbolTable;
	private int staticCount;
	private int fieldCount;
	private int argumentCount;
	private int localCount;
	private String kind;
	private VMWriter vm = new VMWriter();
	
	public CompilationEngine(String outFile, JackTokenizer jt) throws Exception {	
		test = jt; 
		while (jt.hasMoreTokens()) {
			jt.advance();
		// switch statements for calling methods
			System.out.println(jt.getNextToken() + " type: " + jt.tokenType());
			switch (jt.getNextToken()) {
				case ("class"): {
					this.compileClass();
				}
			}
			
			
		}
		
		fw.close();
	
		
	}

		
	

	
	public void compileClass() throws IOException{			
	while (test.hasMoreTokens()) {
		test.advance();
		switch (test.getNextToken()) {
			case ("class"): {
				this.className();
			}
			case ("static"): {
				this.compileClassVarDec();
				kind = "static";
			}
			case ("field"): {
				this.compileClassVarDec();
				kind = "field";
			}
			case ("constructor"): {
				this.compileSubRoutine();
			}
			case ("function"): {
				this.compileSubRoutine();
			}
			case ("method"): {
				this.compileSubRoutine();
			}
			case ("void"): {
				this.compileSubRoutine();
			}
		}

		
	}
	
	
	}
	
	public void className(){
		test.advance();
		className = test.getNextToken();
		globalSymbolTable = new HashMap<String, HashMap<String, HashMap<String, Integer>>>();
	}
	
	public void compileClassVarDec(){
		//fill in global symbol table for static and field vars
		test.advance();
		String type = test.getNextToken();
		test.advance();
		String name = test.getNextToken();
		int currentCount = this.getCorrespondingKindInteger(type);
		HashMap <String, HashMap<String, Integer>> inner1 = new HashMap <String, HashMap<String, Integer>>();
		HashMap <String, Integer> inner2 = new HashMap <String, Integer>();
		
		if (!globalSymbolTable.containsKey(name)) {
			currentCount += 1;
			this.incrementCorrespondingInteger(name);
			inner2.put(kind, currentCount);
			inner1.put(type, inner2);
			globalSymbolTable.put(name, inner1);
		}
		
		while(test.peek().equals(",")) {
			inner1.clear();
			inner2.clear();
			test.advance();
			test.advance();
			name = test.getNextToken();
			if (!globalSymbolTable.containsKey(name)) {
				currentCount += 1;
				this.incrementCorrespondingInteger(name);
				inner2.put(kind, currentCount);
				inner1.put(type, inner2);
				globalSymbolTable.put(name, inner1);
			}
		}
	} 
	
	public void compileSubRoutine() throws IOException{
		//get the type, deal with the subroutine Name, and parameter list
		test.advance(); //next token gives return type
		test.advance();
		subroutineName = test.getNextToken();
		localCount = 0;
		localSymbolTable = new HashMap <String, HashMap<String, HashMap<String, Integer>>>();
		test.advance(); //next token gives (
		while(!test.peek().equals(")")) {
			this.compileParameterList();
		}
		while(test.peek().equals("var")) {
			this.compileVarDec();
		}
		this.compileStatements();
		test.advance();
		if (test.getNextToken().equals("}"))
			return;
	}
	
	public void compileParameterList() {
		
	}
	
	public void compileVarDec() {
		//var int 5;
		//var int 5,6;
		test.advance();
		String type = test.getNextToken(); //int
		test.advance();
		String name = test.getNextToken(); //5
		int currentCount = this.getCorrespondingKindInteger("local");
		HashMap <String, HashMap<String, Integer>> inner1 = new HashMap <String, HashMap<String, Integer>>();
		HashMap <String, Integer> inner2 = new HashMap <String, Integer>();
		
		if (!localSymbolTable.containsKey(name)) {
			currentCount += 1;
			this.incrementCorrespondingInteger("local");
			inner2.put(kind, currentCount);
			inner1.put(type, inner2);
			localSymbolTable.put(name, inner1);
			localCount += 1;
		}
		while(test.peek().equals(",")) {
			inner1.clear();
			inner2.clear();
			test.advance(); //next token gives ,
			test.advance(); 
			name = test.getNextToken(); //6
			if (!localSymbolTable.containsKey(name)) {
				currentCount += 1;
				this.incrementCorrespondingInteger("local");
				inner2.put(kind, currentCount);
				inner1.put("local", inner2);
				localSymbolTable.put(name, inner1);
				localCount += 1;
			}
		}
		test.advance(); //next token gives ;
	}
	
	public void compileStatements() throws IOException {
		test.advance();
		switch(test.getNextToken()) {
			case ("let"): {
				this.compileLet();
			}
			case ("if"): {
				this.compileIf();
			}
			case ("while"): {
				this.compileWhile();
			}
			case ("do"): {
				this.compileDo();
			}
			case ("return"): {
				this.compileReturn();
			}
		}
	}
	
	public void compileDo() {
		
	}
	
	public void compileLet() throws IOException {
		this.CompileExpression();
		

	}
	
	public void compileWhile() {
		
	}
	
	public void compileReturn() {
		//vm.writeReturn();
	}
	
	public void compileIf() {
		
	}
	
	public void CompileExpression() throws IOException {
		this.CompileTerm();
	}
	
	public void CompileTerm() throws IOException {
		
		test.advance();
		switch(test.getToken().toString()) {
		
			case ("stringConstant"): {
				

			}
			case ("integerConstant"): {
				

			}
			case ("keyword"): {
				if (test.getNextToken().equals("true") || test.getNextToken().equals("false") 
					|| test.getNextToken().equals("null") || test.getNextToken().equals("this")){
				
					
				}
			}
			case ("identifier"): {
				
				//An array element using the syntax arrayName[expression]
				if(test.peek().equals("[")){
					
				}
				
				if(test.peek().equals("(")){
					
				}

				else{
					
					
				}
	
}
				
				//A subroutine call that returns a non-void type ()
				// A variable name in scope (the variable may be static, field, local, or a parameter)
			case ("symbol"): {
				
			}
		
		}
		
		
	}

	public void CompileExpressionList() {
		
	}
	
	public int getCorrespondingKindInteger(String k) {
		switch (k) {
		case ("static"):
			return staticCount;
		case ("field"):
			return fieldCount;
		case ("argument"):
			return argumentCount;
		case ("local"):
			return localCount;
		}
		return -1;
	}
	
	public void incrementCorrespondingInteger(String k) {
		switch (k) {
		case ("static"):
			staticCount += 1;
		case ("field"):
			fieldCount += 1;
		case ("argument"):
			argumentCount += 1;
		case ("local"):
			localCount += 1;
		}
	}
	
	
}
