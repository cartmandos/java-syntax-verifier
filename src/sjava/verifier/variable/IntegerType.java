package sjava.verifier.variable;

import sjava.verifier.parser.RegexHandler;
import sjava.verifier.parser.RegexStrings;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.scope.Scope;

import java.lang.*;

public class IntegerType extends Type {
    public IntegerType(String name, String value, Scope scope) throws VariableException, UnexpectedToken {
        super(name, value, scope);
    }

    @Override
    public boolean isLegalValue(String value) {
        return RegexHandler.matches(value, RegexStrings.INTEGER);
    }
}
