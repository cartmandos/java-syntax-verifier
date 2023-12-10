package sjava.verifier.variable;

import sjava.verifier.parser.UnexpectedToken;
import sjava.verifier.parser.VariableAssignmentStmt;
import sjava.verifier.parser.VariableDeclarationStmt;
import sjava.verifier.scope.Scope;

import java.util.LinkedList;

public class VariableFactory {

    public static Variable createVariable(VariableAssignmentStmt variableStmt, Scope scope) throws VariableException, UnexpectedToken {
        String name = variableStmt.getVariableName();
        String value = variableStmt.getValue();

        if (!variableStmt.isDeclaration())
            return null;

        Variable variable;
        switch(variableStmt.getVariableType()) {
                case "boolean":
                    variable = new BooleanType(name, value, scope);
                    break;
                case "char":
                    variable = new CharType(name, value, scope);
                    break;
                case "double":
                    variable = new DoubleType(name, value, scope);
                    break;
                case "int":
                    variable = new IntegerType(name, value, scope);
                    break;
                case "String":
                    variable = new StringType(name, value, scope);
                    break;
                default:
                    throw new IllegalVariableType(variableStmt.getVariableType());
        }
        if (variableStmt.getModifier()!=null) {
            if (value == null)
                throw new UnassignedFinal(name);
            return new FinalModifier(variable);
        }
        return variable;
    }

    public static LinkedList<Variable> createVariables(VariableDeclarationStmt variableStmt, Scope scope) throws VariableException, UnexpectedToken {
        LinkedList<Variable> variables = new LinkedList<>();
        for (String nameAndValue : variableStmt.getVariables()){
            if (nameAndValue.contains(" ")){
                String[] split = nameAndValue.split(" ");
                variables.add(createVariable(new VariableAssignmentStmt(null, variableStmt.getVariableType(), split[0], split[1]), scope));
            }
            else variables.add(createVariable(new VariableAssignmentStmt(null, variableStmt.getVariableType(), nameAndValue, null), scope));
        }
        return variables;
    }

        public static Variable createParameter(String[] args, Scope scope) throws VariableException, UnexpectedToken {
            String modifier=null, type, name;
            if (args.length==3) {
                modifier = args[0];
                type = args[1];
                name = args[2];
            }
            else{
                type = args[0];
                name = args[1];
            }
        Variable variable;
        switch(type) {
            case "boolean":
                variable = new BooleanType(name, null, scope);
                break;
            case "char":
                variable = new CharType(name, null, scope);
                break;
            case "double":
                variable = new DoubleType(name, null, scope);
                break;
            case "int":
                variable = new IntegerType(name, null, scope);
                break;
            case "String":
                variable = new StringType(name, null, scope);
                break;
            default:
                throw new IllegalVariableType(type);
        }
        if (modifier!=null)
            return new FinalModifier(variable);
        return variable;
    }


}
