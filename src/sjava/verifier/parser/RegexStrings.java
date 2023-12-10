package sjava.verifier.parser;

public class RegexStrings {
    public static final String INTEGER = "-?\\d+";
    public static final String DOUBLE = "-?\\d*\\.?\\d+";
    public static final String CHARACTER = "\'.{1}\'";
    public static final String STRING = "\".*\"";
    public static final String BOOLEAN = ReservedWords.TRUE + "|" + ReservedWords.FALSE + "|" + DOUBLE;

    public static final String VARIABLE_NAME = "(?:_\\w+|[a-zA-Z]+\\w*)";

}