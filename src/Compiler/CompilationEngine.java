//leave spaces in between strings
//convert symbols to proper code

package Compiler;

import java.io.*;
import java.util.*;

public class CompilationEngine {

	Scanner in;
	FileReader fr;
	
	private FileWriter fw;
	private JackTokenizer test;
	private VMWriter vm;
	private String className;
	private String subroutineName;
	private String varName;
	
	private String name;
	private String type;
	private String kind;

	private int numExpressions;
	private String tempClassName;
	private String tempSubroutineName;
	
	private SymbolTable allSymbols;
	
	private String op = "+-*/&|<>=";
	private boolean returnCalled = false;

	// private VMWriter vm = new VMWriter();

	public CompilationEngine(String outFile) throws Exception {
		test = new JackTokenizer(outFile);
		vm = new VMWriter(outFile);
		fw = new FileWriter(outFile.replaceAll(".jack", "G.xml"));
		while (test.hasMoreTokens()) {
			test.advance();//masterCount = 1, nextToken is class
			switch (test.getNextToken()) {
			case ("class"): {
				fw.write("<class>");
				fw.write(System.lineSeparator());
				this.compileClass();
			}
			}

		}
		vm.close();
		fw.close();

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
					fw.write("<subroutineDec>");
					fw.write(System.lineSeparator());
					fw.write("<" + test.tokenType() + "> " + "constructor" + " </" + test.tokenType() + ">");
					fw.write(System.lineSeparator());
					this.compileSubRoutine();
					fw.write("</subroutineDec>");
					fw.write(System.lineSeparator());
					break;
				}
				case ("function"): {
					fw.write("<subroutineDec>");
					fw.write(System.lineSeparator());
					fw.write("<" + test.tokenType() + "> " + "function" + " </" + test.tokenType() + ">");
					fw.write(System.lineSeparator());
					this.compileSubRoutine();
					fw.write("</subroutineDec>");
					fw.write(System.lineSeparator());
					break;
				}
				case ("method"): {
					fw.write("<subroutineDec>");
					fw.write(System.lineSeparator());
					fw.write("<" + test.tokenType() + "> " + "method" + " </" + test.tokenType() + ">");
					fw.write(System.lineSeparator());
					this.compileSubRoutine();
					fw.write("</subroutineDec>");
					fw.write(System.lineSeparator());
					break;
				}
				case ("void"): {
					fw.write("<subroutineDec>");
					fw.write("<" + test.tokenType() + "> " + "void" + " </" + test.tokenType() + ">");
					fw.write(System.lineSeparator());
					this.compileSubRoutine();
					fw.write("</subroutineDec>");
					fw.write(System.lineSeparator());
					break;
				}
				default: {
					fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">"); 
					fw.write(System.lineSeparator());
				}
			}
			
		}
		for (String key : allSymbols.global.keySet()) {
			System.out.println(allSymbols.global.get(key).name + " " + allSymbols.typeOf(key) + " " + allSymbols.kindOf(key) 
					+ " " + allSymbols.indexOf(key));
		}
		fw.write("</class>");
		fw.write(System.lineSeparator());
	}

	public void className() throws IOException {
		fw.write("<" + test.tokenType() + "> " + "class" + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		test.advance(); 
		className = test.getNextToken();
		fw.write("<" + test.tokenType() + "> " + className + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		allSymbols = new SymbolTable();
	}

	public void compileClassVarDec() throws IOException {
		// fill in global symbol table for static and field vars
		fw.write("<classVarDec>");
		fw.write(System.lineSeparator());
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //field
		fw.write(System.lineSeparator());
		kind = test.getNextToken();
		test.advance(); 
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //int
		fw.write(System.lineSeparator());
		type = test.getNextToken();
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //x
		fw.write(System.lineSeparator());
		name = test.getNextToken();
		allSymbols.define(name, type, kind);
		while (test.peek().equals(",")) {
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //,
			fw.write(System.lineSeparator());
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //y
			fw.write(System.lineSeparator());
			name = test.getNextToken();
			allSymbols.define(name, type, kind);
		}
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //;
		fw.write(System.lineSeparator());
		fw.write("</classVarDec>");
		fw.write(System.lineSeparator());
	}

	public void compileSubRoutine() throws IOException {
		// get the type, deal with the subroutine Name, and parameter list
		
		allSymbols.startSubroutine();
		test.advance(); // next token gives return type
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //Square
		fw.write(System.lineSeparator());
		test.advance(); // next token gives name of subroutine
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //new
		fw.write(System.lineSeparator());
		subroutineName = test.getNextToken();
		test.advance(); // next token gives (
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		fw.write("<parameterList>" );
		fw.write(System.lineSeparator());
		this.compileParameterList();
		fw.write("</parameterList>" );
		fw.write(System.lineSeparator());
		test.advance(); //next token gives )
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		test.advance(); 
		fw.write("<subroutineBody>");
		fw.write(System.lineSeparator());
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //next token gives {
		fw.write(System.lineSeparator());
		while (test.peek().equals("var")) {
			fw.write("<varDec>" );
			fw.write(System.lineSeparator());
			this.compileVarDec();
			fw.write("</varDec>" );
			fw.write(System.lineSeparator());
		}
		vm.WriteFunction(className + "." + subroutineName, allSymbols.varCount);
		fw.write("<statements>" );
		fw.write(System.lineSeparator());
		while (test.peek().equals("let") || test.peek().equals("if") ||
				test.peek().equals("while") || test.peek().equals("do") || 
				test.peek().equals("return")) {
			this.compileStatements();
		}
		fw.write("</statements>" );
		test.advance();
		fw.write(System.lineSeparator());
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		fw.write("</subroutineBody>");
		fw.write(System.lineSeparator());
		System.out.println(subroutineName);
		for (String key : allSymbols.local.keySet()) {
			System.out.println(key + " " + allSymbols.typeOf(key) + " " + allSymbols.kindOf(key) 
					+ " " + allSymbols.indexOf(key));
		}
		System.out.println();
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
		//System.out.println(test.peek()); 
		while (!test.peek().equals(")")) {
			test.advance();
			type = test.getNextToken();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
			fw.write(System.lineSeparator());
			test.advance();
			name = test.getNextToken();
			kind = "argument";
			allSymbols.define(name, type, kind);
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
			fw.write(System.lineSeparator());
			if (test.peek().equals(",")) {
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
				fw.write(System.lineSeparator());
			}
		}
	}

	public void compileVarDec() throws IOException{
		// var int 5;
		// var int 5,6;
		
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		kind = test.getNextToken(); // var
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		type = test.getNextToken(); // int
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		name = test.getNextToken(); // 5
		allSymbols.define(name, type, kind);
		while (test.peek().equals(",")) {
			test.advance(); // next token gives ,
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
			fw.write(System.lineSeparator());
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
			fw.write(System.lineSeparator());
			name = test.getNextToken(); 
			allSymbols.define(name, type, kind);
		}
		test.advance(); // next token gives ;
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
	}

	public void compileStatements() throws IOException {
		switch (test.peek()) {
		case ("let"): {
			test.advance();
			fw.write("<letStatement>");
			fw.write(System.lineSeparator());
			this.compileLet();
			fw.write("</letStatement>");
			fw.write(System.lineSeparator());
			break;
		}
		case ("if"): {
			test.advance();
			fw.write("<ifStatement>");
			fw.write(System.lineSeparator());
			this.compileIf();
			fw.write("</ifStatement>");
			fw.write(System.lineSeparator());
			break;
		}
		case ("while"): {
			test.advance();
			fw.write("<whileStatement>" );
			fw.write(System.lineSeparator());
			this.compileWhile();
			fw.write("</whileStatement>");
			fw.write(System.lineSeparator());
			break;
		}
		case ("do"): {
			test.advance();
			fw.write("<doStatement>");
			fw.write(System.lineSeparator());
			this.compileDo();
			fw.write("</doStatement>");
			fw.write(System.lineSeparator());
			break;
		}
		case ("return"): {
			returnCalled = true;
			test.advance();
			fw.write("<returnStatement>");
			fw.write(System.lineSeparator());
			this.compileReturn();
			fw.write("</returnStatement>");
			fw.write(System.lineSeparator());
			break;
		}
		}
	}

	//do Screen.setColor(true);
	public void compileDo() throws IOException {
		tempClassName = "";
		tempSubroutineName = "";
		boolean noPeriod = false;
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());		
		while(!test.peek().equals(";")) {
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
			fw.write(System.lineSeparator());
			if (test.peek().equals("(")) {
				tempSubroutineName = test.getNextToken();
			}
			if (test.getNextToken().equals("(")) {
				if (!noPeriod) { //className.subroutineName(expressionList)
					vm.WritePush("pointer", 0);
				}
				this.CompileExpressionList();
				vm.WriteCall(tempClassName + "." + tempSubroutineName, numExpressions);
			}
			else if (test.peek().equals(".")) {
				tempClassName = test.getNextToken();
				noPeriod = true;
			}
		}
		//test.advance();
		//this.compileSubroutineCall();
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		vm.WritePop("temp", 0);
		
	}
	
	public void compileLet() throws IOException {
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //let
		fw.write(System.lineSeparator());
		while(!test.peek().equals(";")) {
			if (test.peek().equals("=")) {
				varName = test.getNextToken();
			}
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">"); //length = 
			fw.write(System.lineSeparator());
			if (test.getNextToken().equals("=") || test.getNextToken().equals("[")) {
				fw.write("<expression>");
				fw.write(System.lineSeparator());
				this.CompileExpression();
				fw.write("</expression>");
				fw.write(System.lineSeparator());
			}
		}
		System.out.println(varName + " varname");
		test.advance();
		/*for (String s : allSymbols.local.keySet()) {
			System.out.println(s);
			System.out.println(varName);
			if (s.equals(varName))
				System.out.println("yrsy");
		}*/
		/*System.out.println("ASDfsadf");
		System.out.println(allSymbols.kindOf(varName));
		if (allSymbols.local.containsKey(varName))
			System.out.println("true");*/
		vm.WritePop(allSymbols.kindOf(varName), allSymbols.indexOf(varName));
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
		fw.write(System.lineSeparator());
	}

	public void compileWhile() throws IOException {
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">"); //let
		fw.write(System.lineSeparator());
		test.advance(); //next token is (
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
		fw.write(System.lineSeparator());
		if (test.getNextToken().equals("(")) {
			fw.write("<expression>");
			fw.write(System.lineSeparator());
			this.CompileExpression();
			fw.write("</expression>");
			fw.write(System.lineSeparator());
		}
		test.advance(); //next token is )
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
		fw.write(System.lineSeparator());
		test.advance(); //next token is {
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
		fw.write(System.lineSeparator());
		fw.write("<statements>" );
		fw.write(System.lineSeparator());
		while (test.peek().equals("let") || test.peek().equals("if") ||
				test.peek().equals("while") || test.peek().equals("do") || 
				test.peek().equals("return")) {
			this.compileStatements();
		}
	fw.write("</statements>" );
	fw.write(System.lineSeparator());
	test.advance();
	fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
	fw.write(System.lineSeparator());
	}
	
	public void compileReturn() throws IOException {
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">"); //return
		fw.write(System.lineSeparator());
		
		if (!test.peek().equals(";")) {
			while(!test.peek().equals(";")) {
					fw.write("<expression>");
					fw.write(System.lineSeparator());
					this.CompileExpression();
					fw.write("</expression>");
					fw.write(System.lineSeparator());
			}
		}
		else {
			vm.WritePush("constant", 0);
			vm.WriteReturn();
		}
		
		
		
		
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
		fw.write(System.lineSeparator());
	}

	public void compileIf() throws IOException{
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">"); //return
			fw.write(System.lineSeparator());	
			test.advance(); //next token is (
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			if (test.getNextToken().equals("(")) {
				fw.write("<expression>");
				fw.write(System.lineSeparator());
				this.CompileExpression();
				fw.write("</expression>");
				fw.write(System.lineSeparator());
			}
			test.advance(); //next token is )
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			test.advance(); //next token is {
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			fw.write("<statements>" );
			fw.write(System.lineSeparator());
			while (test.peek().equals("let") || test.peek().equals("if") ||
					test.peek().equals("while") || test.peek().equals("do") || 
					test.peek().equals("return")) {
				this.compileStatements();
			}
		fw.write("</statements>" );
		fw.write(System.lineSeparator());
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
		fw.write(System.lineSeparator());
		if (test.peek().equals("else")) {
			test.advance(); //next token is else
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			test.advance(); //next token is {
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			fw.write(System.lineSeparator());
			fw.write("<statements>" );
			fw.write(System.lineSeparator());
			while (test.peek().equals("let") || test.peek().equals("if") ||
					test.peek().equals("while") || test.peek().equals("do") || 
					test.peek().equals("return")) {
				this.compileStatements();
			}
			fw.write("</statements>" );
			fw.write(System.lineSeparator());
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
		}
	}

	public void CompileExpression() throws IOException { 
		test.advance(); //next token is x
		fw.write("<term>");
		fw.write(System.lineSeparator());
		this.CompileTerm(); 
		fw.write("</term>");
		fw.write(System.lineSeparator());
		while (op.contains(test.peek())) { 
			test.advance();
			String command = test.getNextToken();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">"); //operation is stored here
			fw.write(System.lineSeparator());
			test.advance();
			fw.write("<term>");
			fw.write(System.lineSeparator());
			this.CompileTerm();
			fw.write("</term>");
			fw.write(System.lineSeparator());
			vm.WriteArithmetic(command);
		}
	}

	public void CompileTerm() throws IOException {
		switch (test.tokenType().toString()) {

		case ("stringConstant"): {
			fw.write("<" + test.tokenType() + "> " + test.stringVal() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			vm.WriteStringConstant(test.stringVal());
			break;
		}
		case ("integerConstant"): {
			fw.write("<" + test.tokenType() + "> " + test.intVal() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			vm.WriteIntegerConstant(test.intVal());
			break;
		}
		case ("keyword"): {
			
			if (test.getNextToken().equals("true")
					|| test.getNextToken().equals("false")
					|| test.getNextToken().equals("null")
					|| test.getNextToken().equals("this")) {
				
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());	
				
				switch (test.getNextToken()){
				case "true":
					break;
				case "false":
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
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				fw.write("<expression>");
				fw.write(System.lineSeparator());
				this.CompileExpression();
				fw.write("</expression>");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
			}
			
			//subroutineName(expessionlist) className.subroutineName varName.subroutineName
			else if (test.peek().equals("(")) {
				/*System.out.println("entered");
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				//test.advance();
				
				this.CompileExpressionList();
				vm.WriteCall(className + "." + subroutineName, numExpressions);
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());*/
				this.compileSubroutineCall();
				
			}
			else if (test.peek().equals(".")) {
				/*fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				this.CompileTerm();*/
				this.compileSubroutineCall();
			}
			//varName
			else {
				//System.out.println("var name " + test.getNextToken());
				vm.WritePush(allSymbols.kindOf(test.getNextToken()), allSymbols.indexOf(test.getNextToken()));
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">"); //i
				fw.write(System.lineSeparator());
			}
			break;
		}

		// A subroutine call that returns a non-void type ()
		// A variable name in scope (the variable may be static, field, local,
		// or a parameter)
		case ("symbol"): {
			if (test.getNextToken().equals("-") || test.getNextToken().equals("~")) {
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<term>");
				fw.write(System.lineSeparator());
				this.CompileTerm();
				fw.write("</term>");
				fw.write(System.lineSeparator());
			}
			else if (test.getNextToken().equals("(")) {
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
				fw.write(System.lineSeparator());
				fw.write("<expression>");
				fw.write(System.lineSeparator());
				this.CompileExpression();
				fw.write("</expression>");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
				fw.write(System.lineSeparator());
			}
			else {
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
				fw.write(System.lineSeparator());
			}
			break;
		}
		}
	}

	//pushes arguments onto stack
	public void CompileExpressionList() throws IOException{
		numExpressions = 0;
		fw.write("<expressionList>" );
		fw.write(System.lineSeparator());
		if (!test.peek().equals(")")) {
			numExpressions += 1;
			fw.write("<expression>");
			fw.write(System.lineSeparator());
			this.CompileExpression(); //goes to next token and compiles expression
			fw.write("</expression>");
			fw.write(System.lineSeparator());
		}
		while (!test.peek().equals(")")) {
			numExpressions += 1;
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
			fw.write(System.lineSeparator());
			fw.write("<expression>");
			fw.write(System.lineSeparator());
			this.CompileExpression(); //goes to next token and compiles expression
			fw.write("</expression>");
			fw.write(System.lineSeparator());
		}
		fw.write("</expressionList>");
		fw.write(System.lineSeparator());
	}
	
	public static void main(String[] args) {
		try {
			CompilationEngine e = new CompilationEngine("Main.jack");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
