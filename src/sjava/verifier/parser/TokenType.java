package sjava.verifier.parser;

public enum TokenType {
    VARIABLE("int|boolean|char|double|String"),
    RETURNTYPE("void"),
    RETURN("return"),
    CONDITION("if|while"),
    WHITESPACE("\\s+|\\t"),
    COMMA("\\,"),
    EQUAL("\\="),
    OPENPAR("\\("),
    CLOSEPAR("\\)"),
    SUFFIX("\\{|\\;"),
    OP("\\|\\||&&"),
    FINAL("final"),
    TRUEORFALSE("true|false"),
    UNRECOGNIZED(null);

    public final String pattern;

    TokenType(String pattern) {
        this.pattern = pattern;
    }
}
