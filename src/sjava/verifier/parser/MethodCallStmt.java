package sjava.verifier.parser;

import sjava.verifier.scope.ClassScope;
import sjava.verifier.scope.MethodScope;
import sjava.verifier.scope.ScopeException;
import sjava.verifier.variable.Variable;

import java.util.LinkedList;
import java.util.List;

public class MethodCallStmt extends Statement {
    private String methodName;
    private LinkedList<String> parameters = new LinkedList<>();

    public MethodCallStmt(String methodName, LinkedList<String> parameters) {
        super(StatementType.METHOD_CALL);
        this.methodName = methodName;
        if (parameters!=null)
            this.parameters = parameters;
    }

    public LinkedList<String> getParameters() {
        return parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public List<? super String> getArguments() {
        return null;
    }

    public void execute(ClassScope classScope) throws ScopeException {
        MethodScope methodToCall = classScope.findMethod(methodName);
        if (methodToCall!=null)
            methodToCall.checkSignature(parameters);
    }
}
