package Compiler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class VMWriter {
private FileWriter fw;

public VMWriter(String outputFile) throws IOException {
	outputFile = outputFile.replace(".jack", ".vm");
	fw = new FileWriter(outputFile);
}

public void WritePush(String token, int index) throws IOException {
	fw.write("push " + token + " " + Integer.toString(index));
	fw.write(System.lineSeparator());
}

public void WritePop(String token, int index) throws IOException {
	fw.write("pop " + token + " " + Integer.toString(index));
	fw.write(System.lineSeparator());
}

public void WriteArithmetic(String command) throws IOException {
	 switch (command) {
	
	 case "+":
		 fw.write("add");
		 fw.write(System.lineSeparator());
		 break;
	 case "-":
		 fw.write("sub");
		 fw.write(System.lineSeparator());
		 break;
	 case "*":
		 fw.write("call Math.multiply 2");
		 fw.write(System.lineSeparator());
		 break;
	 case "/":
		 fw.write("call Math.divide 2");
		 fw.write(System.lineSeparator());
		 break; 
		
	 }
}

public void WriteLabel(String label) throws IOException {
	fw.write("label " + label);
	fw.write(System.lineSeparator());
}

public void WriteGoto(String label) throws IOException {
	fw.write("goto" + label);
	fw.write(System.lineSeparator());
}

public void WriteIf(String label) throws IOException {
	fw.write("if-goto" + label);
	fw.write(System.lineSeparator());
}

public void WriteCall(String name, int numArgs) throws IOException {
	fw.write("call " + name + " " + numArgs);
	fw.write(System.lineSeparator());
}

public void WriteFunction(String name, int numLocals) throws IOException {
	fw.write("function " + name + " " + numLocals);
	fw.write(System.lineSeparator());
}

public void WriteReturn() throws IOException {
	fw.write("return");
	fw.write(System.lineSeparator());
}

public void WriteStringConstant(String val) throws IOException {
	fw.write("push constant " + Integer.toString(val.length()));
	fw.write(System.lineSeparator());
	fw.write("call String.new 1");
	fw.write(System.lineSeparator());
	for (int i = 0; i < val.length(); i++) {
		int x = (int) val.charAt(i);
		fw.write("push constant " + Integer.toString(x));
		fw.write(System.lineSeparator());
		fw.write("call String.appendChar 2");
		fw.write(System.lineSeparator());
	}
}

public void WriteIntegerConstant(int val) throws IOException {
	fw.write("push constant " + Integer.toString(val));
	fw.write(System.lineSeparator());
}

public void close() throws IOException {
	fw.close();
}


}
