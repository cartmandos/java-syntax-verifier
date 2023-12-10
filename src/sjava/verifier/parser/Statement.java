package sjava.verifier.parser;

import java.util.List;

public abstract class Statement {

    private void setIdentifier(StatementType identifier) {
        this.identifier = identifier;
    }

    public enum StatementType {
        METHOD_DECLARATION, METHOD_CALL, VARIABLE_DECLARATION,
        VARIABLE_ASSIGNMENT, CONDITION_BLOCK, END_OF_BLOCK, RETURN
    }

    private StatementType identifier;

    public Statement(StatementType type) {
        this.setIdentifier(type);
    }

    public StatementType getIdentifier() {
        return identifier;
    }

    public abstract List<? super String> getArguments();
}
