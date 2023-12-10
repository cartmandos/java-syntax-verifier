package sjava.verifier.parser;

public class ExpectedCloseBracket extends ParseException {
    public ExpectedCloseBracket(String scope, int lineNumber) {
        super("Missing close bracket in the end of "+scope, lineNumber);
    }
}
