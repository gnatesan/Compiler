package Compiler;

public enum variableCount {
	STATIC(0), ARGUMENT (0), VAR (0), FIELD(0), NONE(0);
	
	int value;
	
    variableCount(int value) {
        this.value = value;
    }
    
   int value() {
    	return value;
    }
   
   void increment() {
	   value = value + 1;
   }
    
   
}
