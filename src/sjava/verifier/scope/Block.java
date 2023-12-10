package sjava.verifier.scope;

import sjava.verifier.parser.Statement;

import java.util.LinkedList;

public abstract class Block extends Scope {

    protected LinkedList<Statement> methodCalls;
    public LinkedList<String> conditions;

    public Block(Scope parentScope) {
        super(parentScope);
    }

    public void addMethodCall(Statement methodCall){
        if (methodCalls==null)
            methodCalls = new LinkedList<>();
        this.methodCalls.add(methodCall);
    }

}
