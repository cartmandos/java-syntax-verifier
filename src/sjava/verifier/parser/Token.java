package sjava.verifier.parser;

public class Token {
    public TokenType type;
    public String data;

    public Token(TokenType type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getData(){
        return data;
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", type.name(), data);
    }
}
