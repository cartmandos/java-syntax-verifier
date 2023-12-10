package sjava.verifier.scope;

public class IllegalMethodName extends ScopeException {
    public IllegalMethodName(String methodName) {
        super(methodName);
    }
}
