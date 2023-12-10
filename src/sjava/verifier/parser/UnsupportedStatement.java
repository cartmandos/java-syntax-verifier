package sjava.verifier.parser;

public class UnsupportedStatement extends ParseException {
    public UnsupportedStatement(String statement, String scope, int lineNumber) {
        super(statement+" is not supported in "+scope+" scope.", lineNumber);
    }
}
