package sjava.verifier.scope;

import java.util.LinkedList;

public class WhileBlock extends Block implements ConditionBlock {

    public WhileBlock(Scope parentScope, LinkedList<String> conditions) {
        super(parentScope);
    }

    @Override
    public void checkCondition() {
    }
}
