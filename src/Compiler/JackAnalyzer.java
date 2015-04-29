//import java.io.File;
package Compiler;
import java.io.File;
//have a compilationengine object for each ".jack" file, constuctor for CE should only take in the 
//name of the jack file. inside CE constructor, instantiate VMWriter and JackTokenizer object
//with the parameter as the name of the jack file
public class JackAnalyzer {
	
	public static void main (String[] args) throws Exception { //Z:/Elements/
			File dir = new File("C:/Compiler/ConvertToBin");
			if (dir.isFile()) {
				System.out.println("asdsdsdff");
				CompilationEngine cs = new CompilationEngine (dir.getName());
			}
			else if (dir.isDirectory()) {
				System.out.println("asdf");
				for (File f : dir.listFiles()) {
					if (f.getName().endsWith(".jack")) {
						System.out.println("d" + f.getName() + "d");
						CompilationEngine cs = new CompilationEngine ("C:/Compiler/ConvertToBin/" + f.getName());
					}
				}
			}

			

	}

}
