package sjava.verifier.scope;

import sjava.verifier.parser.MethodCallStmt;
import sjava.verifier.parser.Statement;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.variable.IllegalValue;
import sjava.verifier.variable.IllegalVariableName;
import sjava.verifier.variable.IllegalVariableType;
import sjava.verifier.variable.UnassignedFinal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class ClassScope extends Scope{
    private String name;

    protected HashMap<String, MethodScope> methods = new HashMap<>();

    public void addMethod(MethodScope method) throws ScopeException {
        if (methods.containsKey(method.getName()))
            throw new ScopeException(method.getName() +" already defined in scope.");
        this.methods.put(method.getName(), method);
    }

    public ClassScope(Scope parentScope, String name) {
        super(parentScope);
        this.name=name;
    }

    public String getName(){ return this.name; }

    public HashMap<String, MethodScope> getMethods() {
        return methods;
    }

    public MethodScope findMethod(String methodName){
        return methods.get(methodName);
    }

    @Override
    public LinkedList<Scope> getScopes() {
        LinkedList<Scope> allscopes = new LinkedList<>();
        allscopes.addAll(methods.values());
        if (scopes!=null)
            allscopes.addAll(scopes);
        return allscopes;
    }

    public Iterator<Scope> iterator(){
        LinkedList<Scope> scopes = getScopes();
        return scopes.iterator();
    }
}
