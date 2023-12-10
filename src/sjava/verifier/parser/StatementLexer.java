package sjava.verifier.parser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatementLexer {
    public static LinkedList<Token> lex(String input) {
        //trimming start&end spaces - no need to check indentation nor in-line comments supported
        input=input.trim();
        // The tokens to return
        LinkedList<Token> tokens = new LinkedList<>();

        // Lexer logic begins here
        StringBuilder tokenPatternsBuffer = new StringBuilder();
        for (TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        Pattern tokenPatterns = Pattern.compile(tokenPatternsBuffer.substring(1));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        int lastMatchEnd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            if (matcher.start() != lastMatchEnd)
                tokens.add(new Token(TokenType.UNRECOGNIZED, input.substring(lastMatchEnd, start)));

            lastMatchEnd = matcher.end();

            for (TokenType tokenType : TokenType.values()) {
                if (matcher.group().matches(tokenType.pattern)) {
                    tokens.add(new Token(tokenType, matcher.group()));
                    break;
                }
            }
        }
        if (lastMatchEnd != input.length())
            tokens.add(new Token(TokenType.UNRECOGNIZED, input.substring(lastMatchEnd, input.length())));
        return tokens;
    }
}
