package sjava.verifier.variable;

import sjava.verifier.parser.RegexHandler;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.scope.Scope;

import java.lang.*;
import java.util.LinkedList;

public abstract class Type implements Variable{
    protected String name;
    protected String value;
    protected LinkedList<VariableAssignment> varAssi = new LinkedList<>();

    public LinkedList<VariableAssignment> getVarAssi() {
        return varAssi;
    }

    public Type(String name, String value, Scope scope) throws VariableException, UnexpectedToken {
        if (!isLegalName(name))
            throw new IllegalVariableName(name);
        this.name=name;
        if (value!=null) {
            if (isLegalValue(value))
                this.varAssi.add(new VariableAssignment(value, null, this));
            else if (isLegalName(value))
                this.varAssi.add(new VariableAssignment(value, scope, this));
            else throw new IllegalValue(value);
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isLegalName(String name) {
        return (name==null || RegexHandler.matches(name, "(?:_\\w+|[a-zA-Z]+\\w*)"));
    }

    public abstract boolean isLegalValue(String value);

    public void assign(String value) throws IllegalValue {
        if (!isLegalValue(value))
            throw new IllegalValue(value);

        this.value=value;

    }

    public void assign(String value, Scope scope) throws VariableException, UnexpectedToken {
//        if (!isLegalValue(value))
//            throw new IllegalValue(value);

        // check if value is another var
        // check if value is another var
        if (isLegalName(value)){

            //problem with changing globals in a method

            if (scope.findVar(value)!=null) {
                if(scope.findVar(value).getValue()==null)
                    throw new VariableException("stupid check");
                assign( scope.findVar(value).getValue());
                return;
            }
            Scope upperScope = scope;
            while ((upperScope = upperScope.parent) !=null) {
                if (upperScope.findVar(value) != null) {
                    if(upperScope.findVar(value).getValue()==null)
                        throw new VariableException("stupid check");
                    assign( upperScope.findVar(value).getValue());
                    return;
                }
            }
            throw new UnexpectedToken(value+" var not defined");

        }
    }
}
