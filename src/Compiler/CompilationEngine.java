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
	
	private String name;
	private String type;
	private String kind;

	private SymbolTable allSymbols;
	
	private String op = "+-*/&|<>=";
	

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
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());		
		while(!test.peek().equals(";")) {
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
			fw.write(System.lineSeparator());
			if (test.getNextToken().equals("(")) {
				this.CompileExpressionList();
			}
		}
		test.advance();
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" );
		fw.write(System.lineSeparator());
		//vm.WritePop("temp", 0);
		
	}
	
	public void compileLet() throws IOException {
		fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">" ); //let
		fw.write(System.lineSeparator());
		while(!test.peek().equals(";")) {
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
		test.advance();
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
		while(!test.peek().equals(";")) {
			//test.advance(); //next token is this
			if (!test.peek().equals("return")) {
				fw.write("<expression>");
				fw.write(System.lineSeparator());
				this.CompileExpression();
				fw.write("</expression>");
				fw.write(System.lineSeparator());
			}
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
		test.advance(); //next token is i
		fw.write("<term>");
		fw.write(System.lineSeparator());
		this.CompileTerm(); 
		fw.write("</term>");
		fw.write(System.lineSeparator());
		while (op.contains(test.peek())) { 
			test.advance();
			fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			test.advance();
			fw.write("<term>");
			fw.write(System.lineSeparator());
			this.CompileTerm();
			fw.write("</term>");
			fw.write(System.lineSeparator());
		}
	}

	public void CompileTerm() throws IOException {
		switch (test.tokenType().toString()) {

		case ("stringConstant"): {
			fw.write("<" + test.tokenType() + "> " + test.stringVal() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			//vm.WriteStringConstant(test.stringVal());
			break;
		}
		case ("integerConstant"): {
			fw.write("<" + test.tokenType() + "> " + test.intVal() + " </" + test.tokenType() + ">");
			fw.write(System.lineSeparator());
			//vm.WriteIntegerConstant(test.intVal());
			break;
		}
		case ("keyword"): {
			if (test.getNextToken().equals("true")
					|| test.getNextToken().equals("false")
					|| test.getNextToken().equals("null")
					|| test.getNextToken().equals("this")) {
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
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
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				//test.advance();
				
				this.CompileExpressionList();
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				
			}
			else if (test.peek().equals(".")) {
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				fw.write("<" + test.tokenType() + "> " + test.getNextToken() + " </" + test.tokenType() + ">");
				fw.write(System.lineSeparator());
				test.advance();
				this.CompileTerm();
			}
			//varName
			else {
				//System.out.println("var name " + test.getNextToken());
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

	public void CompileExpressionList() throws IOException{
		fw.write("<expressionList>" );
		fw.write(System.lineSeparator());
		if (!test.peek().equals(")")) {
			fw.write("<expression>");
			fw.write(System.lineSeparator());
			this.CompileExpression(); //goes to next token and compiles expression
			fw.write("</expression>");
			fw.write(System.lineSeparator());
		}
		while (!test.peek().equals(")")) {
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
			CompilationEngine e = new CompilationEngine("SquareGame.jack");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
