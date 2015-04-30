package Compiler;

import java.io.File;
import java.io.IOException;
public class JackAnalyzer {
	
	
	public JackAnalyzer(String fileName) throws Exception {
		File dir = new File(fileName);
		if (dir.isFile()) {
			CompilationEngine cs = new CompilationEngine (dir.getPath());
		}
		else if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				if (f.getName().endsWith(".jack")) {
					CompilationEngine cs = new CompilationEngine (f.getPath());
					System.out.println(f.getPath());
				}
			}
		}
	}
	
	public static void main (String[] args) { 
		try {
			JackAnalyzer j = new JackAnalyzer(args[0]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

	}

}
