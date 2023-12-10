package sjava.verifier.parser;

import java.util.LinkedList;
import java.util.List;

public class ConditionBlockStmt extends Statement {

    private String conditionType;
    private LinkedList<String> conditions;

    public ConditionBlockStmt(String conditionType, LinkedList<String> conditions) {
        super(StatementType.CONDITION_BLOCK);
        this.conditionType = conditionType;
        this.conditions = conditions;
    }

    public LinkedList<String> getConditions() {
        return conditions;
    }

    public String getConditionType() {
        return conditionType;
    }

    @Override
    public List<? super String> getArguments() {
        return conditions;
    }
}
