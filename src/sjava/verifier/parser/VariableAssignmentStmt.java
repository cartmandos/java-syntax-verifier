package sjava.verifier.parser;

import sjava.verifier.scope.Scope;
import sjava.verifier.scope.ScopeException;
import sjava.verifier.variable.*;

import java.util.LinkedList;
import java.util.List;

public class VariableAssignmentStmt extends Statement {

    private String modifier;
    private String variableType;
    private String variableName;
    private String value;

    public VariableAssignmentStmt(String modifier, String variableType, String variableName, String value) {
        super(StatementType.VARIABLE_ASSIGNMENT);
        this.modifier=modifier;
        this.variableType=variableType;
        this.variableName=variableName;
        this.value=value;
    }

    public boolean isDeclaration(){
        return variableType != null;
    }

    public void execute(Scope scope) throws VariableException, ScopeException, UnexpectedToken {
        //problem with changing globals in a method
        if (isDeclaration())
            scope.addVar(VariableFactory.createVariables((VariableDeclarationStmt) getDeclaration(), scope));

        if (scope.findVar(variableName)!=null) {
            scope.findVar(variableName).assign(value, scope);
            return;
        }
        Scope upperScope = scope;
        while ((upperScope = upperScope.parent) !=null) {
            if (upperScope.findVar(variableName) != null) {
//                scope.addVar(upperScope.findVar(variableName));
                scope.addVar(upperScope.findVar(variableName));
                scope.findVar(variableName).assign(value, upperScope);
                return;
            }
        }
        throw new UnexpectedToken(variableName+" var not defined");
    }

    public boolean isAssigningVariable(){
        return RegexHandler.matches(variableName, "(?:_\\w+|[a-zA-Z]+\\w*)");
    }

    public void findVar(){

    }


    public Statement getDeclaration(){
        LinkedList<String> variables = new LinkedList<>();
        variables.add(variableName+" "+value);
        return new VariableDeclarationStmt(modifier, variableType, variables);
    }

    public String getModifier() {
        return modifier;
    }

    public String getValue() {
        return value;
    }

    public String getVariableType() {
        return variableType;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public List<? super String> getArguments() {
        return null;
    }
}
