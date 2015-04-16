package Compiler;


public class JackAnalyzer {
	
	private String inputFile = "Square.jack";
	private String outputFile = "test";
	
	
	public static void main (String[] args) throws Exception {
			JackAnalyzer ja = new JackAnalyzer();
			JackTokenizer jt = new JackTokenizer(ja.inputFile);
			CompilationEngine cs = new CompilationEngine (ja.outputFile, jt);

			

	}

}
