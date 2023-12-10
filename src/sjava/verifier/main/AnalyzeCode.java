package sjava.verifier.main;

import sjava.verifier.parser.MethodCallStmt;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.parser.VariableAssignmentStmt;
import sjava.verifier.scope.ClassScope;
import sjava.verifier.scope.MethodScope;
import sjava.verifier.scope.Scope;
import sjava.verifier.scope.ScopeException;
import sjava.verifier.variable.*;

public class AnalyzeCode {

    private static ClassScope mainScope;

    public static void analyzeScope(ClassScope sJavaScope) throws ScopeException, VariableException, UnexpectedToken {
        mainScope = sJavaScope;
        execute(mainScope);

// RUN
        mainScope = sJavaScope;
        executeMethodCalls();
        executeVariableAssignment(mainScope);
        System.out.println("ok");
    }

    public static void executeMethodCalls() throws ScopeException {
        if (mainScope.getMethods() != null) {
            for (MethodScope method : mainScope.getMethods().values())
                if (method.getMethodCalls()!=null)
                    for (MethodCallStmt call : method.getMethodCalls())
                        call.execute(mainScope);
        }
    }

    public static void executeVariableAssignment(Scope scope) throws VariableException, ScopeException, UnexpectedToken {
        if (scope == null)
            return;
        else {
            for (Scope innerScope : scope)
                executeVariableAssignment(innerScope);
        }
        for (VariableAssignmentStmt ass : scope.getAssignmentStmts())
            ass.execute(scope);
    }

    public static void execute(Scope scope) throws VariableException, ScopeException, UnexpectedToken {
        if (scope == null)
            return;
        scope.execute();
        for (Scope innerScope : scope)
                execute(innerScope);
    }
}
