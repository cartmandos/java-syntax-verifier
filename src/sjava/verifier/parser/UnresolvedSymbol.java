package sjava.verifier.parser;

public class UnresolvedSymbol extends ParseException {
    public UnresolvedSymbol(String s, int lineNumber) {
        super(s, lineNumber);
    }
}
