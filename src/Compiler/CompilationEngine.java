//leave spaces in between strings
//convert symbols to proper code

package Compiler;

import java.io.*;
import java.util.*;

public class CompilationEngine {

	Scanner in;
	FileReader fr;
	
	private JackTokenizer test;
	private VMWriter vm;
	private String className;
	private String subroutineName;
	private String varName;
	
	private String name;
	private String type;
	private String kind;
	
	private int numWhile;
	private int numIf;
	private int numArgs;
	
	private int numExpressions;
	private String tempClassName;
	private String tempSubroutineName;
	
	private SymbolTable allSymbols;
	
	private String op = "+-*/&|<>=";
	private boolean returnCalled = false;

	public CompilationEngine(String outFile) throws Exception {
		test = new JackTokenizer(outFile);
		String replace = outFile.replace(".jack", ".vm");
		vm = new VMWriter(replace);
		while (test.hasMoreTokens()) {
			test.advance();//masterCount = 1, nextToken is class
			switch (test.getNextToken()) {
			case ("class"): {
				this.compileClass();
			}
			}

		}
		vm.close();

	}

	public void compileClass() throws IOException {
		this.className();
		while (test.hasMoreTokens()) {
			test.advance();

			switch (test.getNextToken()) {
				case ("static"): {
					kind = "static";
					this.compileClassVarDec();
					break;
				}
				case ("field"): {
					kind = "field";
					this.compileClassVarDec();
					break;
				} //variable declarations in a function, method, constructor
				case ("constructor"): {
					this.compileSubRoutine();
					break;
				}
				case ("function"): {
					this.compileSubRoutine();
					break;
				}
				case ("method"): {
					this.compileSubRoutine();
					break;
				}
				case ("void"): {
					this.compileSubRoutine();
					break;
				}
				default: {
				}
			}
			
		}
	}

	public void className() throws IOException {
		test.advance(); 
		className = test.getNextToken();
		allSymbols = new SymbolTable();
	}

	public void compileClassVarDec() throws IOException {
		// fill in global symbol table for static and field vars
		kind = test.getNextToken();
		test.advance(); 
		type = test.getNextToken();
		test.advance();
		name = test.getNextToken();
		allSymbols.define(name, type, kind);
		while (test.peek().equals(",")) {
			test.advance();
			test.advance();
			name = test.getNextToken();
			allSymbols.define(name, type, kind);
		}
		test.advance();
	}

	public void compileSubRoutine() throws IOException {
		// get the type, deal with the subroutine Name, and parameter list
		String name = test.getNextToken();
		allSymbols.startSubroutine();
			test.advance(); // next token gives return type
			test.advance(); // next token gives name of subroutine
			subroutineName = test.getNextToken();
			test.advance(); // next token gives (
			this.compileParameterList();
			test.advance(); //next token gives )
			test.advance(); 
			while (test.peek().equals("var")) {
				this.compileVarDec();
			}
			vm.WriteFunction(className + "." + subroutineName, allSymbols.varCount);
			if (name.equals("constructor")) {
				vm.WritePush("constant", allSymbols.argCount);
				vm.WriteCall("Memory.alloc", 1);
				vm.WritePop("pointer", 0);
			}
			else if (name.equals("method")) {
				vm.WritePush("argument", allSymbols.argCount);
				vm.WritePop("pointer", 0);
			}
			while (test.peek().equals("let") || test.peek().equals("if") ||
					test.peek().equals("while") || test.peek().equals("do") || 
					test.peek().equals("return")) {
				this.compileStatements();
			}
			test.advance();
		}

	public void compileSubroutineCall() throws IOException {
		String tempClassName = "";
		String tempSubroutineName = "";
		if (test.peek().equals("(")) { 
			vm.WritePush("pointer", 0);
			tempSubroutineName = test.getNextToken();
			test.advance();
			this.CompileExpressionList();
			vm.WriteCall(className + "." + tempSubroutineName, numExpressions);
		}
		else {
			if (allSymbols.indexOf(test.getNextToken()) == -1) {
				tempClassName = test.getNextToken();
				test.advance(); //next token is .
				test.advance();
				tempSubroutineName = test.getNextToken();
				test.advance();
				this.CompileExpressionList();
				vm.WriteCall(tempClassName + "." + tempSubroutineName, numExpressions);
			}
			else {
				tempClassName = allSymbols.typeOf(test.getNextToken());
				vm.WritePush(allSymbols.kindOf(test.getNextToken()), allSymbols.indexOf(test.getNextToken()));
				test.advance(); //next token is .
				test.advance();
				tempSubroutineName = test.getNextToken();
				test.advance();
				this.CompileExpressionList();
				vm.WriteCall(tempClassName + "." + tempSubroutineName, numExpressions);
			}
		}
	}
	
	public void compileParameterList() throws IOException {
		numArgs = 0;
		while (!test.peek().equals(")")) {
			numArgs += 1;
			test.advance();
			type = test.getNextToken();
			test.advance();
			name = test.getNextToken();
			kind = "argument";
			allSymbols.define(name, type, kind);
			if (test.peek().equals(",")) {
				test.advance();
			}
		}
	}

	public void compileVarDec() throws IOException{
		// var int 5;
		// var int 5,6;
		
		test.advance();
		kind = test.getNextToken(); // var
		test.advance();
		type = test.getNextToken(); // int
		test.advance();
		name = test.getNextToken(); // 5
		allSymbols.define(name, type, kind);
		while (test.peek().equals(",")) {
			test.advance(); // next token gives ,
			test.advance();
			name = test.getNextToken(); 
			allSymbols.define(name, type, kind);
		}
		test.advance(); // next token gives ;
	}

	public void compileStatements() throws IOException {
		switch (test.peek()) {
		case ("let"): {
			test.advance();
			this.compileLet();
			break;
		}
		case ("if"): {
			test.advance();
			this.compileIf();
			break;
		}
		case ("while"): {
			test.advance();
			this.compileWhile();
			break;
		}
		case ("do"): {
			test.advance();
			this.compileDo();
			break;
		}
		case ("return"): {
			returnCalled = true;
			test.advance();
			this.compileReturn();
			break;
		}
		}
	}

	public void compileDo() throws IOException {
		String tempClassNamet = "";
		String tempSubroutineNamet = "";
		String tempVarNamet = "";
		boolean Period = false;	
		while(!test.peek().equals(";")) {
			test.advance();
			if (test.peek().equals("(")) {
				tempSubroutineNamet = test.getNextToken();
			}
			if (test.getNextToken().equals("(")) {
				if (!Period) { //className.subroutineName(expressionList)
					vm.WritePush("pointer", 0);
				}
				this.CompileExpressionList();
				vm.WriteCall(tempClassNamet + "." + tempSubroutineNamet, numExpressions);
			}
			else if (test.peek().equals(".")) {
					if (allSymbols.indexOf(test.getNextToken()) != -1 ) { //local variable
						tempVarNamet = test.getNextToken();
						vm.WritePush(allSymbols.kindOf(tempVarNamet), numExpressions);
						tempClassNamet = allSymbols.typeOf(test.getNextToken());
					}
					else { //className
						
						tempClassNamet = test.getNextToken();
					}
				Period = true;
			}
		}
		test.advance();
		vm.WritePop("temp", 0);
		
	}
	
	public void compileLet() throws IOException {
		while(!test.peek().equals(";")) {
			if (test.peek().equals("=")) {
				varName = test.getNextToken();
			}
			test.advance();
			if (test.getNextToken().equals("=") || test.getNextToken().equals("[")) {
				this.CompileExpression();
			}
		}
		test.advance();
		vm.WritePop(allSymbols.kindOf(varName), allSymbols.indexOf(varName));
	}

	public void compileWhile() throws IOException {
		int tempWhile = numWhile;
		vm.WriteLabel("WHILE_" + Integer.toString(numWhile));
		numWhile++;
		test.advance(); //next token is (
		if (test.getNextToken().equals("(")) {
			this.CompileExpression();
		}
		test.advance(); //next token is )
		vm.WriteArithmetic("not");
		vm.WriteIf("WHILE_END_" + Integer.toString(tempWhile));
		test.advance(); //next token is {
		while (test.peek().equals("let") || test.peek().equals("if") ||
				test.peek().equals("while") || test.peek().equals("do") || 
				test.peek().equals("return")) {
			this.compileStatements();
		}
	vm.WriteGoto("WHILE_" + Integer.toString(tempWhile));
	vm.WriteLabel("WHILE_END_" + Integer.toString(tempWhile));
	test.advance();
	}
	
	public void compileReturn() throws IOException {
		if (!test.peek().equals(";")) {
			while(!test.peek().equals(";")) {
					this.CompileExpression();
			}
			vm.WriteReturn();
		}
		else {
			vm.WritePush("constant", 0);
			vm.WriteReturn();
		}
		
		
		test.advance();
	}

	public void compileIf() throws IOException{
			int tempIf = numIf;
			numIf++;
			test.advance(); //next token is (
			if (test.getNextToken().equals("(")) {
				this.CompileExpression();
			}
			test.advance(); //next token is )
			vm.WriteArithmetic("not");
			vm.WriteIf("IF_FALSE_" + Integer.toString(tempIf));
			test.advance(); //next token is {
			while (test.peek().equals("let") || test.peek().equals("if") ||
					test.peek().equals("while") || test.peek().equals("do") || 
					test.peek().equals("return")) {
				this.compileStatements();
			}
			vm.WriteGoto("IF_END_" + Integer.toString(tempIf));
			vm.WriteLabel("IF_FALSE_" + Integer.toString(tempIf));
		test.advance();
		if (test.peek().equals("else")) {
			test.advance(); //next token is else
			test.advance(); //next token is {
			while (test.peek().equals("let") || test.peek().equals("if") ||
					test.peek().equals("while") || test.peek().equals("do") || 
					test.peek().equals("return")) {
				this.compileStatements();
			}
			test.advance();
		}
		vm.WriteLabel("IF_END_" + Integer.toString(tempIf));
	}

	public void CompileExpression() throws IOException { 
		test.advance(); //next token is -
		this.CompileTerm(); 
		while (op.contains(test.peek())) {
			test.advance();
			String command = test.getNextToken();
			test.advance();
			this.CompileTerm();
			vm.WriteArithmetic(command);
		}
	}

	public void CompileTerm() throws IOException {
		switch (test.tokenType().toString()) {

		case ("stringConstant"): {
			vm.WriteStringConstant(test.stringVal());
			break;
		}
		case ("integerConstant"): {
			vm.WriteIntegerConstant(test.intVal());
			break;
		}
		case ("keyword"): {
			
			if (test.getNextToken().equals("true")
					|| test.getNextToken().equals("false")
					|| test.getNextToken().equals("null")
					|| test.getNextToken().equals("this")) {
				
				switch (test.getNextToken()){
				case "true":
					vm.WritePush("constant", 0);
					vm.WriteArithmetic("not");
					break;
				case "false":
					vm.WritePush("constant", 0);
					break;
				case "null":
					break;
				case "this":
					break;
				}
				
				
			}
			break;
		}
		case ("identifier"): {
			// An array element using the syntax varName[expression]
			if (test.peek().equals("[")) { //let sum = sum + a[i];
				test.advance();
				this.CompileExpression();
				test.advance();
			}
			
			//subroutineName(expessionlist) className.subroutineName varName.subroutineName
			else if (test.peek().equals("(")) {
				this.compileSubroutineCall();
				
			}
			else if (test.peek().equals(".")) {
				this.compileSubroutineCall();
			}
			//varName
			else {
				vm.WritePush(allSymbols.kindOf(test.getNextToken()), allSymbols.indexOf(test.getNextToken()));
			}
			break;
		}

		// A subroutine call that returns a non-void type ()
		// A variable name in scope (the variable may be static, field, local,
		// or a parameter)
		case ("symbol"): {
			if (test.getNextToken().equals("-") || test.getNextToken().equals("~")) {
				String unary = test.getNextToken();
				test.advance();
				this.CompileTerm();
				if (unary.equals("-")) {
					vm.WriteArithmetic("neg");
				}
				else {
					vm.WriteArithmetic("not");
				}
			}
			else if (test.getNextToken().equals("(")) {
				this.CompileExpression();
				test.advance();
			}
			else {
			}
			break;
		}
		}
	}

	//pushes arguments onto stack
	public void CompileExpressionList() throws IOException{
		numExpressions = 0;
		if (!test.peek().equals(")")) {
			numExpressions += 1;
			this.CompileExpression(); //goes to next token and compiles expression
		}
		while (!test.peek().equals(")")) {
			numExpressions += 1;
			test.advance();
			this.CompileExpression(); //goes to next token and compiles expression
		}
	}

}
