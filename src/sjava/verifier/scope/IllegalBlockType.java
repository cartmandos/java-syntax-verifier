package sjava.verifier.scope;

public class IllegalBlockType extends ScopeException {
    public IllegalBlockType(String methodName) {
        super(methodName);
    }
}
