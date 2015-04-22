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
	private String kind;
	
	private int staticCount;
	private int fieldCount;
	private int argumentCount;
	private int variableCount;
	
	private int staticLocal;
	private int fieldLocal;
	private int argumentLocal;
	private int variableLocal;
	
	private SymbolTable global;
	private SymbolTable local;
	//private VMWriter vm = new VMWriter();
	
	
	
	
	
	
	public CompilationEngine(String outFile, JackTokenizer jt) throws Exception {	
		test = jt; 
		fw = new FileWriter(outFile);
		while (test.hasMoreTokens()) {
			test.advance();
			fw.write(test.getNextToken()); // WRITING TO TEST FILE!
			
		// switch statements for calling methods
			System.out.println(test.getNextToken() + " type: " + test.tokenType());
			switch (test.getNextToken()) {
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
		//fw.write(test.getNextToken()); // WRITING TO TEST FILE!
		
		switch (test.getNextToken()) {
			case ("class"): {
				this.className();
			}
			case ("static"): {
				kind = "static";
				this.compileClassVarDec();
				
			}
			case ("field"): {
				kind = "field";
				this.compileClassVarDec();
				
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
	
	public void className() throws IOException{
		test.advance();
		//fw.write(test.getNextToken()); // WRITING TO TEST FILE!
		className = test.getNextToken();
		global = new SymbolTable();
	}
	
	public void compileClassVarDec() throws IOException{
		//fill in global symbol table for static and field vars
		test.advance();
		String type = test.getNextToken();
		test.advance();
	
		String name = test.getNextToken();
		//int currentCount = this.varCount(type);
		if (!global.getTest().containsKey(name)) {
			global.define(name, type, kind, this.varCount(kind));
			//currentCount += 1;
			this.incrementCount(kind);
		}
		
		while(test.peek().equals(",")) {
			test.advance();
			test.advance();
			name = test.getNextToken();
			if (!global.getTest().containsKey(name)) {
				global.define(name, type, kind, this.varCount(kind));
				//currentCount += 1;
				this.incrementCount(kind);
			}
		}
	} 
	
	public void compileSubRoutine() throws IOException{
		//get the type, deal with the subroutine Name, and parameter list
		this.localClear();
		local.startSubroutine();
		test.advance(); //next token gives return type
		test.advance(); //next token gives name of subroutine
		subroutineName = test.getNextToken();
		local = new SymbolTable();
		test.advance(); //next token gives (
		while(!test.peek().equals(")")) {
			this.compileParameterList();
		}
		test.advance(); //next token gives )
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
		String kind = test.getNextToken(); //var
		test.advance();
		String type = test.getNextToken(); //int
		test.advance();
		String name = test.getNextToken(); //5
		//int currentCount = this.getCorrespondingKindInteger("local");
		if (!local.getTest().containsKey(name)) {
			local.define(name, type, kind, this.varLocal(kind));
			this.incrementLocal(kind);
		}
		while(test.peek().equals(",")) {
			test.advance(); //next token gives ,
			test.advance(); 
			name = test.getNextToken(); //6
			if (!local.getTest().containsKey(name)) {
				local.define(name, type, kind, this.varLocal(kind));
				this.incrementLocal(kind);
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
	
	public int varCount(String kind) {
		switch(kind) {
		case("static"): {
			return staticCount;
		}
		case("field"): {
			return fieldCount;
		}
		case("var"): {
			return variableCount;
		}
		case("argument"): {
			return argumentCount;
		}
		}
		return -1;
	}
	
	public void incrementCount(String kind) {
		switch(kind) {
		case("static"): {
			staticCount += 1;
		}
		case("field"): {
			fieldCount += 1;
		}
		case("var"): {
			variableCount += 1;
		}
		case("argument"): {
			argumentCount += 1;
		}
		}
	}
	
	public int varLocal(String kind) {
		switch(kind) {
		case("static"): {
			return staticLocal;
		}
		case("field"): {
			return fieldLocal;
		}
		case("var"): {
			return variableLocal;
		}
		case("argument"): {
			return argumentLocal;
		}
		}
		return -1;
	}
	
	public void incrementLocal(String kind) {
		switch(kind) {
		case("static"): {
			staticLocal += 1;
		}
		case("field"): {
			fieldLocal += 1;
		}
		case("var"): {
			variableLocal += 1;
		}
		case("argument"): {
			argumentLocal += 1;
		}
		}
	}
	
	public void localClear() {
		variableLocal = 0;
		fieldLocal = 0;
		argumentLocal = 0;
		staticLocal = 0;
	}
	
	
}
