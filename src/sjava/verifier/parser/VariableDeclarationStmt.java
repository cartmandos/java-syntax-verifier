package sjava.verifier.parser;

import java.util.LinkedList;
import java.util.List;

public class VariableDeclarationStmt extends Statement{

    private String modifier=null;
    private String variableType;
    private LinkedList<String> variables;

    public VariableDeclarationStmt(String variableType, LinkedList<String> variables) {
        super(StatementType.VARIABLE_DECLARATION);
        this.variableType=variableType;
        this.variables=variables;
    }

    public VariableDeclarationStmt(String modifier, String variableType, LinkedList<String> variables) {
        this(variableType, variables);
        this.modifier = modifier;
    }

    public String getVariableType() {
        return variableType;
    }

    public LinkedList<String> getVariables() {
        return variables;
    }

    @Override
    public List<? super String> getArguments() {
        return null;
    }

    public String getModifier(){
        return modifier;
    }
}
