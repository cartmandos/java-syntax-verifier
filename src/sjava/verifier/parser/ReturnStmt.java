package sjava.verifier.parser;

import java.util.List;

public class ReturnStmt extends Statement {
    public ReturnStmt(){
        super(StatementType.RETURN);
    }
    private String returnType;

    public ReturnStmt(String returnType) {
        super(StatementType.RETURN);
        this.returnType = returnType;
    }

    @Override
    public List<? super String> getArguments() {
        return null;
    }
}
