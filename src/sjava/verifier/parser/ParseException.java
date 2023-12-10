package sjava.verifier.parser;

public class ParseException extends Exception {
    public ParseException(String s, int lineNumber){
        super("Line: " +lineNumber+ ", " +s);
    }
    public ParseException(int lineNumber){
        super("Line: " +lineNumber);
    }

    public ParseException(String s){ super(s);}
}
