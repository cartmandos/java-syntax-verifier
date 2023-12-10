package sjava.verifier.parser;

public class MissingReturnStatement extends ParseException {
    public MissingReturnStatement(int lineNumber) {
        super("Missing return statement in the end of a method.", lineNumber);
    }
}
