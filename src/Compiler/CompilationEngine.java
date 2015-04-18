package Compiler;

import java.io.*;
import java.util.*;

// VM translations in chapter 9

public class CompilationEngine {
	
	
	Scanner in;
	FileReader fr;
	
	private String line;
	private FileWriter fw;
	private JackTokenizer test;
	
	public CompilationEngine(String outFile, JackTokenizer jt) throws Exception {	
		test = jt;
		fw = new FileWriter(outFile); 
		while (jt.hasMoreTokens()) {
			jt.advance();
		// switch statements for calling methods
			System.out.println(jt.getNextToken() + " type: " + jt.tokenType());
			switch (jt.getNextToken()) {
				case ("class"): {
					this.compileClass();
				}
				case ("while"): {
					this.compileWhile();
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
			}
			case ("field"): {
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
	
	public void className(){

	}
	
	public void compileClassVarDec(){
		
		
		
	} 
	
	public void compileSubRoutine(){
		//get the type, deal with the subroutine Name, and parameter list
		this.compileParameterList();
		this.compileStatements();
	}
	
	public void compileParameterList() {
		
	}
	
	public void compileVarDec() {
		
		
	}
	
	public void compileStatements() {
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
	
	public void compileLet() {
			
	}
	
	public void compileWhile() {
		
	}
	
	public void compileReturn() {
		
	}
	
	public void compileIf() {
		
	}
	
	public void CompileExpression() {
		
	}
	
	public void CompileTerm() {
		
	}

	public void CompileExpressionList() {
		
	}
	
	
}
