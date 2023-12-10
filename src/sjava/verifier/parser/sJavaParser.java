package sjava.verifier.parser;

import sjava.verifier.scope.*;
import sjava.verifier.variable.VariableException;
import sjava.verifier.variable.VariableFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;

import static sjava.verifier.parser.Statement.*;

public class sJavaParser {

    private static LineNumberReader lnr;

    public static Scope parseSJava(File sJava) throws IOException, ParseException, ScopeException, VariableException {
        try {
            lnr = new LineNumberReader(new FileReader(sJava));
            return classParser();
        } finally {
            lnr.close();
        }
    }

    private static ClassScope classParser() throws IOException, ParseException, ScopeException, VariableException {
        // allows globals & methods
        ClassScope classScope = new ClassScope(null,"main");
        String commendableLine;
        while ((commendableLine = readCommendableLine()) != null){
            Statement statement = StatementParser.parse(commendableLine, lnr.getLineNumber());
            switch (statement.getIdentifier()){
                case METHOD_DECLARATION:
                    classScope.addMethod(MethodParser(classScope, statement));
                    break;
                case VARIABLE_DECLARATION:
                    classScope.addVar(VariableFactory.createVariables((VariableDeclarationStmt) statement, classScope));
                    break;
                case VARIABLE_ASSIGNMENT:
                    classScope.addVariableAssignment(statement);
//
//                    classScope.addVar(VariableFactory.createVariable((VariableAssignmentStmt) statement));
                    break;
                default:
                    throw new UnsupportedStatement(statement.getIdentifier().name(), "class", lnr.getLineNumber());
            }
        }
        return classScope;
    }

    private static MethodScope MethodParser(Scope parent, Statement methodSignature) throws IOException, ParseException, ScopeException, VariableException {
        // allows vars & blocks
        MethodScope methodScope = new MethodScope(parent, (MethodDeclarationStmt) methodSignature);
        String commendableLine;
        Statement statement = null;
        while ((commendableLine = readCommendableLine()) != null && !isCloseBracket(commendableLine)){
            statement = StatementParser.parse(commendableLine, lnr.getLineNumber());
            switch (statement.getIdentifier()){
                case RETURN: //can add return to method - not obliged
                    methodScope.addReturnCall(statement);
                    break;
                case METHOD_CALL:
                    methodScope.addMethodCall(statement);
                    break;
                case VARIABLE_DECLARATION:
                    methodScope.addVar(VariableFactory.createVariables((VariableDeclarationStmt) statement, methodScope));
                    break;
                case VARIABLE_ASSIGNMENT:
                    methodScope.addVariableAssignment(statement);
//                    methodScope.addVar(VariableFactory.createVariable((VariableAssignmentStmt) statement));
                    break;
                case CONDITION_BLOCK:
                    methodScope.addScope(BlockParser(methodScope, statement));
                    break;
                default:
                    throw new UnsupportedStatement(statement.getIdentifier().name(), "method", lnr.getLineNumber());
            }
        }
        if(commendableLine == null)
            throw new ExpectedCloseBracket("method", lnr.getLineNumber());
        else if (statement == null || !StatementType.RETURN.equals(statement.getIdentifier()))
            throw new MissingReturnStatement(lnr.getLineNumber());

        return methodScope;
    }

    private static Scope BlockParser(Scope parent, Statement blockStatement) throws ParseException, IOException, ScopeException, VariableException {
        // allows vars & blocks
        Block blockScope = BlockFactory.createBlock(parent, (ConditionBlockStmt) blockStatement);
        String commendableLine;
        Statement statement;
        while ((commendableLine = readCommendableLine()) != null && !isCloseBracket(commendableLine)){
            statement = StatementParser.parse(commendableLine, lnr.getLineNumber());
            switch (statement.getIdentifier()){
                case RETURN:
                    break;
                case METHOD_CALL:
                    blockScope.addMethodCall(statement);
                    break;
                case VARIABLE_DECLARATION:
                    blockScope.addVar(VariableFactory.createVariables((VariableDeclarationStmt) statement, blockScope));
                    break;
                case VARIABLE_ASSIGNMENT:
                    blockScope.addVariableAssignment(statement);
//                    blockScope.addVar(VariableFactory.createVariable((VariableAssignmentStmt) statement));
                    break;
                case CONDITION_BLOCK:
                    blockScope.addScope(BlockParser(blockScope, statement));
                    break;
                default:
                    throw new UnsupportedStatement(statement.getIdentifier().name(), "block", lnr.getLineNumber());
            }
        }
        if(commendableLine == null)
            throw new ExpectedCloseBracket("block", lnr.getLineNumber());

        return blockScope;
    }

    private static boolean isCloseBracket(String line) {
        return line.trim().length()==1 && line.trim().charAt(0)=='}';

    }

    private static String readCommendableLine() throws IOException {
        String line;
        while ((line = lnr.readLine()) != null) {
            if (!RegexHandler.matches(line, RegexHandler.EMPTY_LINE) &&
                    !RegexHandler.lookingAt(line, RegexHandler.COMMENT_PREFIX))
                return line;
        }
        return null;
    }
}
