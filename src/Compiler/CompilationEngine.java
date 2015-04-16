package Compiler;

import java.io.*;
import java.util.*;

// VM translations in chapter 9

public class CompilationEngine {
	
	
	Scanner in;
	FileReader fr;
	
	private String line;
	private FileWriter fw;
	private static ArrayList<String> type; // NEED TO FILL OUT!
	private static ArrayList<String> subroutineDec; // NEED TO FILL OUT!
	private static ArrayList<String> classVarDec; // NEED TO FILL OUT!
	
	public CompilationEngine(String outFile, JackTokenizer jt) throws Exception {	
		fw = new FileWriter(outFile); 
		
		fw.write(System.lineSeparator());
		while (jt.hasMoreTokens()) {
			jt.advance();
		// switch statements for calling methods
			System.out.println(jt.getNextToken());
			System.out.println(jt.tokenType());
			
		//	fw.write(jt.tokenType() + " " + jt.getNextToken());
			//fw.write(System.lineSeparator());
			
			
		}
		
		fw.close();
		}
		
	
	/*public void Cot= tokenizer.get();
		assert += "class"
		parseClassname();
		t = get(); // {
		while(peek() is in {field|static}){
			parseClassVarDec()
		}
		while(peek() is in {constructor | function | }){
			parseSubRoutineDec();
		}
			
		get(); // }mpileSubroutine() {
		
	}*/
	
	public void compileClass() throws IOException{
		
			//fw.write();
			//className();
		//	compileClassVarDec();
		//	while (classVarDec.contains(jt.getNextToken()) ){
		//		compileVarDec();
		//	}
			
		//	while (subroutineDec.contains(jt.getNextToken())){
		//		compileVarDec(); // NEED TO FILL OUT!
		//	}
			
			
		
	}
	
	public void className() throws IOException{
	//	 jt.advance();
		// fw.write(jt.getNextToken()); // The class name
		// jt.advance();
		// fw.write(jt.getNextToken()); // This will take the "{"
		
	}
	public void compileClassVarDec() throws IOException{
		
		//while(jt.getNextToken() != ";"){
		// fw.write(jt.getNextToken());
		// jt.advance();
		// } 
		
	//fw.write(jt.getNextToken());
	//jt.advance();
	} 
	
	public void compileSubRoutine(){
		
		
		
	}
	public void compileParameterList() {
		
	}
	
	public void compileVarDec() {
		
	}
	
	public void compileStatements() {
		
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
