package sjava.verifier.parser;

import java.util.regex.*;

public class RegexHandler {

    /* A regex. Represents an empty line */
    public final static String EMPTY_LINE = "\\s*";

    /* A regex. Represents a comment line  */
    public final static String COMMENT_PREFIX = "\\s*//";

    public static boolean matches(String line, String regExp) {
        return regExpStringMatcher(line, regExp).matches();
    }

    public static boolean lookingAt(String line, String regExp) {
        return regExpStringMatcher(line, regExp).lookingAt();
    }


    public static Matcher regExpStringMatcher(String line, String regExp) {
        Pattern pattern = Pattern.compile(regExp);
        return pattern.matcher(line);
    }
}
