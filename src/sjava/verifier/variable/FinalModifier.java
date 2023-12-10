package sjava.verifier.variable;

import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.scope.Scope;

import java.util.LinkedList;

public class FinalModifier extends Modifier {
    private Variable variable;

    public FinalModifier(Variable variable) throws UnassignedFinal {
        this.variable = variable;
    }

    @Override
    public String getName() {
        return variable.getName();
    }

    @Override
    public String getValue() {
        return variable.getValue();
    }

    @Override
    public boolean isLegalName(String name) {
        // this is code rep. in a way, but in java finals have different name convention
        return variable.isLegalValue(name);
    }

    @Override
    public boolean isLegalValue(String value) {
        return variable.isLegalValue(value);
    }

    @Override
    public void assign(String value) throws UnassignedFinal {
        throw new UnassignedFinal("final cannot be reassigned.");
    }

    @Override
    public void assign(String value, Scope scope) throws IllegalValue, UnexpectedToken, UnassignedFinal {
        throw new UnassignedFinal("final cannot be reassigned.");
    }

    @Override
    public LinkedList<VariableAssignment> getVarAssi() {
        return null;
    }

    public boolean isAssigned(){
        return this.variable.getValue() != null;
    }
}
