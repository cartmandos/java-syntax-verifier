package sjava.verifier.variable;

public class IllegalVariableType extends VariableException {
    public IllegalVariableType(String lineNumber) {
        super(lineNumber);
    }
}
