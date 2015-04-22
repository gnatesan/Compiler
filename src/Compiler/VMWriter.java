package Compiler;

import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
private FileWriter fw;




public VMWriter(String outputFile) throws IOException{
	fw = new FileWriter(outputFile);
}

public void WritePop( String token, int index){
	//fw.write("pop " +   )
}

public void WriteArithmetic(){
	
}

public void WriteLabel(){
	
}

public void WriteGoto(){
	
}

public void WriteIf(){
	
}

public void WriteCall(){
	
}

public void WriteFunction(){
	
}

public void WriteReturn() {
	// TODO Auto-generated method stub
	//fw.write("pop " +   )
}



public void close(){
	
}


}
