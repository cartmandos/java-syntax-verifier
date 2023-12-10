package sjava.verifier.variable;

import sjava.verifier.parser.RegexHandler;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.scope.Scope;

public class VariableAssignment {
    Scope scopeToAssign;
    String value;
    Variable var;

    public VariableAssignment(String value, Scope scope, Variable var) {
        this.scopeToAssign=scope;
        this.value=value;
        this.var=var;
    }

    public VariableAssignment(String value, Scope scope) {
    }

    public void execute() throws VariableException, UnexpectedToken {
        if (scopeToAssign==null)
            var.assign(value);
        else var.assign(value, scopeToAssign);
    }
}
