package sjava.verifier.variable;

import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.scope.Scope;

import java.util.LinkedList;

public interface Variable {

    String getName();
    String getValue();

    boolean isLegalName(String name);
    boolean isLegalValue(String value);

    void assign(String value) throws IllegalValue, UnassignedFinal;

    void assign(String value, Scope scope) throws VariableException, UnexpectedToken;
    public LinkedList<VariableAssignment> getVarAssi();
}
