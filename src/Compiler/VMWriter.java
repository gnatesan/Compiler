package Compiler;

import java.io.FileWriter;
import java.io.IOException;

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

public void WriteArithmetic() throws IOException {
	
}

public void WriteLabel(String label) throws IOException {
	
}

public void WriteGoto(String label) throws IOException {
	
}

public void WriteIf(String label) throws IOException {
	
}

public void WriteCall(String name, int numArgs) throws IOException {
	
}

public void WriteFunction(String name, int numLocals) throws IOException {
	
}

public void WriteReturn() throws IOException {
	
}

public void WriteStringConstant(String val) throws IOException {
	/*fw.write("push constant " + Integer.toString(val.length()));
	fw.write(System.lineSeparator());
	fw.write("call String.new 1");
	fw.write(System.lineSeparator());
	for (int i = 1; i < val.length(); i++) {
		
	}*/
}

public void WriteIntegerConstant(int val) throws IOException {
	fw.write("push constant " + Integer.toString(val));
	fw.write(System.lineSeparator());
}

public void close() throws IOException {
	fw.close();
}


}
