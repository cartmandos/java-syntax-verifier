package sjava.verifier.scope;

import sjava.verifier.parser.Statement;
import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.parser.VariableAssignmentStmt;
import sjava.verifier.variable.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class Scope implements Iterable<Scope> {

    public Scope parent;

    public Scope(Scope parentScope){
        this.parent = parentScope;
    }

//    protected HashMap<String, Scope> scopes1 = new HashMap<>();
    protected HashMap<String, Variable> variables = new HashMap<>();
//
//    public void addScope1(Scope)


    protected LinkedList<VariableAssignmentStmt> assignmentStmts = new LinkedList<>();
    protected LinkedList<Scope> scopes = new LinkedList<>();

    public void addScope(Scope scope){
        if (scopes==null)
            scopes = new LinkedList<>();
        this.scopes.add(scope);
    }

    public void addVar(Variable var) throws ScopeException {
        if (var==null)
            return;
        if (variables.containsKey(var.getName()))
            throw new ScopeException(var.getName() +" already defined in scope.");
        this.variables.put(var.getName(), var);
    }
    public void addVar(LinkedList<Variable> vars) throws ScopeException {
        for (Variable var : vars)
            addVar(var);
    }

    public void addVariableAssignment(Statement statement){
        this.assignmentStmts.add((VariableAssignmentStmt) statement);
    }

    public LinkedList<VariableAssignmentStmt> getAssignmentStmts() {
        return assignmentStmts;
    }

    public LinkedList<Scope> getScopes() {
        return scopes;
    }


    public Iterator<Scope> iterator(){
        return this.scopes.iterator();
    }

    public Variable findVar(String name){
        return variables.get(name);

    }

    public void execute() throws UnexpectedToken, VariableException, ScopeException {
        if (variables!=null)
            for(Variable var: variables.values())
                if(var.getVarAssi()!=null)
                    for (VariableAssignment asi : var.getVarAssi())
                        asi.execute();
        for (VariableAssignmentStmt ass : assignmentStmts)
            ass.execute(this);
    }
}
