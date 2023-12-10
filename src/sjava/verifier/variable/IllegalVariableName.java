package sjava.verifier.variable;

public class IllegalVariableName extends VariableException {

    public IllegalVariableName(String lineNumber) {
        super(lineNumber);
    }
}
