package Compiler;

//have a compilationengine object for each ".jack" file, constuctor for CE should only take in the 
//name of the jack file. inside CE constructor, instantiate VMWriter and JackTokenizer object
//with the parameter as the name of the jack file
public class JackAnalyzer {
	
	public static void main (String[] args) throws Exception {
			CompilationEngine cs = new CompilationEngine (args[0]);

			

	}

}
