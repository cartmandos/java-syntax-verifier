package sjava.verifier.variable;

public class UnassignedFinal extends VariableException {
    public UnassignedFinal(String name) {
        super(name);
    }
}
