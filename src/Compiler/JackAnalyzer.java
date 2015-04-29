//import java.io.File;
package Compiler;
import java.io.File;
//have a compilationengine object for each ".jack" file, constuctor for CE should only take in the 
//name of the jack file. inside CE constructor, instantiate VMWriter and JackTokenizer object
//with the parameter as the name of the jack file
public class JackAnalyzer {
	
	public static void main (String[] args) throws Exception {
			File dir = new File("Z:/Elements/Compiler/Seven");
			if (dir.isFile()) {
				CompilationEngine cs = new CompilationEngine (dir.getName());
			}
			else if (dir.isDirectory()) {
				for (File f : dir.listFiles()) {
					if (f.getName().endsWith(".jack")) {
						System.out.println(f.getName());
						CompilationEngine cs = new CompilationEngine (f.getName());
					}
				}
			}

			

	}

}
