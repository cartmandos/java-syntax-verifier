package sjava.verifier.scope;

import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.variable.IllegalValue;
import sjava.verifier.variable.UnassignedFinal;

public interface ConditionBlock {
    void checkCondition() throws IllegalValue, UnexpectedToken, UnassignedFinal, IllegalExpression;

    //        if (!RegexHandler.matches(ex.getData(), RegexStrings.VARIABLE_NAME+"|"+
//                RegexStrings.DOUBLE+"|"+RegexStrings.INTEGER+"|"+"true\\|false"))
//            throw new IllegalExpression("cannot resolve "+ex.getData(), lineNumber);
}
