package sjava.verifier.parser;

import java.util.LinkedList;
import java.util.List;

public class MethodDeclarationStmt extends Statement {

    private String returnType;
    private String methodName;
    private LinkedList<String> parameters;


    public MethodDeclarationStmt(String returnType, String methodName, LinkedList<String> parameters) {
        super(StatementType.METHOD_DECLARATION);
        this.returnType = returnType;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public LinkedList<String> getParameters() {
        return parameters;
    }

    public String getReturnType() {
        return returnType;
    }


    @Override
    public List<? super String> getArguments() {
        return parameters;
    }
}
