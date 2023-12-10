package sjava.verifier.parser;

public class UnexpectedToken extends ParseException {
    public UnexpectedToken(int lineNumber) {
        super(lineNumber);
    }

    public UnexpectedToken(String data, int lineNumber) {
        super("Unexpected token: "+data, lineNumber);
    }

    public UnexpectedToken(String s) {
        super(s);

    }
}
