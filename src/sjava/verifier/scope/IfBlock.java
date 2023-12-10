package sjava.verifier.scope;

import sjava.verifier.parser.RegexHandler;
import sjava.verifier.parser.RegexStrings;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.variable.*;

import java.util.LinkedList;

public class IfBlock extends Block implements ConditionBlock {



    public IfBlock(Scope parentScope, LinkedList<String> conditions) throws IllegalExpression, IllegalValue, UnexpectedToken, UnassignedFinal {
        super(parentScope);
        this.conditions=conditions;
    }

    public String getValue(String value, Scope scope) throws IllegalValue, UnexpectedToken, UnassignedFinal {
        if (scope.findVar(value)!=null) {
            return scope.findVar(value).getValue();
        }
        Scope upperScope = scope;
        while ((upperScope = upperScope.parent) !=null) {
            if (upperScope.findVar(value) != null) {
                return upperScope.findVar(value).getValue();
            }
        }
        throw new UnexpectedToken(value+" var not defined");
    }

    @Override
    public void checkCondition() throws IllegalValue, UnexpectedToken, UnassignedFinal, IllegalExpression {
        String value="";
        for (String s : conditions){
            if (RegexHandler.matches(s, RegexStrings.VARIABLE_NAME))
                value=this.getValue(s, this.parent);
            if (value==null || !RegexHandler.matches(s, RegexStrings.BOOLEAN))
                throw new IllegalExpression("Cannot resolve "+s);
        }

    }

    public void execute() throws VariableException, ScopeException, UnexpectedToken {
        super.execute();
        checkCondition();

    }
}
