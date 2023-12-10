package sjava.verifier.scope;

import sjava.verifier.parser.MethodCallStmt;
import sjava.verifier.parser.MethodDeclarationStmt;
import sjava.verifier.parser.Statement;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.variable.*;

import java.util.LinkedList;

public class MethodScope extends Scope{

    private String name;
    private String returnType;

    private LinkedList<MethodCallStmt> methodCalls = new LinkedList<>();
    private LinkedList<Statement> returnCalls;
    private LinkedList<Variable> parameters = new LinkedList<>();


    public MethodScope(Scope parentScope, MethodDeclarationStmt methodSignature) throws ScopeException, VariableException, UnexpectedToken {
        super(parentScope);
        this.name = methodSignature.getMethodName();
        this.returnType = methodSignature.getReturnType();
        if (methodSignature.getParameters()!=null) {
            for (String parameter : methodSignature.getParameters())
                addParam(VariableFactory.createParameter(parameter.split(" "), this));
            addVar(this.parameters);
        }
    }

    private void addParam(Variable var){
        parameters.add(var);
    }

    public void addMethodCall(Statement methodCall){
        if (methodCalls==null)
            methodCalls = new LinkedList<>();
        this.methodCalls.add((MethodCallStmt) methodCall);
    }

    public void addReturnCall(Statement methodCall){
        if (returnCalls==null)
            returnCalls = new LinkedList<>();
        this.returnCalls.add(methodCall);
    }

    public String getName() {
        return name;
    }

    public LinkedList<MethodCallStmt> getMethodCalls() {
        return methodCalls;
    }

    public LinkedList<Variable> getParameters() {
        return parameters;
    }

    public boolean checkSignature(LinkedList<String> parameters) throws ScopeException {
//        if (parameters==null)
//            if (this.parameters==null)
//                return true;

        if (parameters.size()!=this.parameters.size())
            throw new ScopeException("different number of params");
        for (int i =0; i<parameters.size();i++)
            if (!this.parameters.get(i).isLegalValue(parameters.get(i)))
                throw new ScopeException("Param value not match method sig");
        return true;
    }

    public void execute() throws VariableException, ScopeException, UnexpectedToken {
        super.execute();
        for (MethodCallStmt call : methodCalls)
            call.execute((ClassScope) parent);
    }
}
