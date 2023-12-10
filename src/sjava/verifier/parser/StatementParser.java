package sjava.verifier.parser;

import java.util.Iterator;
import java.util.LinkedList;

public class StatementParser {

    private static LinkedList<Token> tokens;
    private static Iterator<Token> tokenIterator;

    //should probably not be here
    private static int lineNumber;

    public static Statement parse(String line, int lineNumber) throws EmptyLine, UnresolvedSymbol, UnexpectedToken {
        StatementParser.lineNumber = lineNumber;
        tokens = StatementLexer.lex(line);
        tokenIterator = tokens.iterator();

        Token firstToken = tokens.getFirst();

        if (firstToken == null)
            throw new EmptyLine(lineNumber);
        Statement statement = null;

        switch (firstToken.type){
            case RETURNTYPE:
                statement = parseMethodDeclaration();
                break;
            case CONDITION:
                statement = parseConditionBlock();
                break;
            case VARIABLE:
                statement = parseVariableDeclaration();
                break;
            case RETURN:
                statement = parseReturn();
                break;
            case FINAL:
            case UNRECOGNIZED:
                if(tokens.size()<3)
                    throw new UnexpectedToken(firstToken.getData());
                else if (tokens.get(1).type == TokenType.OPENPAR || tokens.get(2).type == TokenType.OPENPAR)
                    statement = parseMethodCall();
                else statement = parseVariableAssignment();
                break;
        }
        String endOfLine = getEndOfLine();
        if (endOfLine.length()!=0)
            throw new UnexpectedToken(endOfLine, lineNumber);
        return statement;
    }

    private static Statement parseReturn() throws UnexpectedToken {
        // no return types...
        getNextToken();
        checkSuffix(';');
        return new ReturnStmt("void");
    }

    private static Statement parseVariableDeclaration() throws UnexpectedToken {
        String variableType = parseVariableType();
        LinkedList<String> variables = new LinkedList<>();
        Token nextToken;
        do {
            String name = parseVariableName();
            nextToken = getNextNotWhiteSpace();
            if (nextToken.type == TokenType.EQUAL) {
                variables.add(name+" "+parseValue());
                nextToken = getNextNotWhiteSpace();
            }
            else variables.add(name);
        } while(nextToken.type == TokenType.COMMA);
        if (!(nextToken.type == TokenType.SUFFIX))
            throw new UnexpectedToken("miss open parenthesis", lineNumber);

        return new VariableDeclarationStmt(variableType, variables);

        //print
//        System.out.println(variableType+" : ");
//        System.out.println(variables);
    }

    private static Statement parseVariableAssignment() throws UnexpectedToken {
        String modifier = parseModifier();
        String variableType=null;
        if (modifier!=null)
            variableType = parseVariableType();
        String variableName = parseVariableName();
        checkEqual();
        String value = parseValue();
        checkSuffix(';');

        return new VariableAssignmentStmt(modifier, variableType, variableName, value);

        //print
//        System.out.println(modifier+" "+variableType+" "+variableName+" "+value);
    }

    private static Statement parseMethodCall() throws UnexpectedToken {
        String methodName = parseMethodName();
        LinkedList<String> parameters = parseMethodCallParameters();
        checkSuffix(';');

        return new MethodCallStmt(methodName, parameters);

        //print
//        System.out.println(methodName);
//        for (StringType s: parameters)
//            System.out.println(s);
    }

    private static Statement parseConditionBlock() throws UnexpectedToken {
        String conditionType = getNextExceptedToken().getData();
        LinkedList<String> conditions = parseConditionExpression();
        checkSuffix('{');

        return new ConditionBlockStmt(conditionType, conditions);

        //print
//        System.out.println(conditionType+" ");
//        for (StringType s: conditions)
//            System.out.println(s);
    }

    private static Statement parseMethodDeclaration() throws UnexpectedToken {
        String returnType = parseReturnType();
        String methodName = parseMethodName();
        LinkedList<String> parameters = parseParameters();
        checkSuffix('{');

//        print
//        System.out.println(returnType+" "+methodName);
//        for (String s: parameters)
//            System.out.println(s);

        return new MethodDeclarationStmt(returnType, methodName, parameters);
    }

    private static String getEndOfLine() throws UnexpectedToken {
        Token endOfLine;
        String eol= "";
        while ((endOfLine=getNextToken()) != null)
            eol += endOfLine.getData();
        return eol;
    }

    private static String parseVariableType() throws UnexpectedToken {
        String variableType = getNextExceptedToken().getData();
        checkNextWhiteSpace();
        return variableType;
    }

    private static String parseModifier() throws UnexpectedToken {
        Token modifier = getNextExceptedToken();
        if (modifier.type == TokenType.FINAL) {
            checkNextWhiteSpace();
            return modifier.getData();
        }
        tokenIterator = tokens.iterator();
        return null;
    }

    private static String parseValue() throws UnexpectedToken {
        Token value = getNextNotWhiteSpace();
        //reserved words&chars
        if (value.type!=TokenType.UNRECOGNIZED && value.type!=TokenType.TRUEORFALSE)
            throw new UnexpectedToken(value.getData(), lineNumber);

//        if (!RegexHandler.matches(value, RegexStrings.BOOLEAN+"|"+RegexStrings.INTEGER+"|"+RegexStrings.CHARACTER
//                +"|"+RegexStrings.DOUBLE+"|"+RegexStrings.STRING))
//            throw new IllegalValue(value, lineNumber);
        return value.getData();
    }

    private static void checkEqual() throws UnexpectedToken {
        Token checkedToken = getNextNotWhiteSpace();
        if (checkedToken.type != TokenType.EQUAL)
            throw new UnexpectedToken(checkedToken.getData(), lineNumber);
    }

    private static String parseVariableName() throws UnexpectedToken {
        //changed exppected->nonspace
        Token variableName = getNextNotWhiteSpace();

        //reserved words&chars
        if (variableName.type!=TokenType.UNRECOGNIZED)
            throw new UnexpectedToken(variableName.getData(), lineNumber);
//
//        if (!RegexHandler.matches(variableName, "(?:_\\w+|[a-zA-Z]+\\w*)"))
//            throw new IllegalMethodName(variableName, lineNumber);
        return variableName.getData();
    }

    private static LinkedList<String> parseConditionExpression() throws UnexpectedToken {
        if (getNextNotWhiteSpace().type != TokenType.OPENPAR)
            throw new UnexpectedToken("missing open par", lineNumber);
        Token lastToken=null;
        LinkedList<String> conditions = new LinkedList<>();
        do{
            if (lastToken != null)
                conditions.add(lastToken.getData());
            conditions.add(getExpression());
        }while((lastToken=getNextNotWhiteSpace()).type == TokenType.OP);
        if (lastToken.type!=TokenType.CLOSEPAR)
            throw new UnexpectedToken("missing closepar", lineNumber);
        return conditions;
    }

    private static String getExpression() throws UnexpectedToken {
        Token ex = getNextNotWhiteSpace();
        if (ex.type == TokenType.CLOSEPAR)
            throw new UnexpectedToken("Expression expected", lineNumber);

        if (ex.type!=TokenType.TRUEORFALSE && ex.type!=TokenType.UNRECOGNIZED)
            throw new UnexpectedToken(ex.getData(), lineNumber);

//        if (!RegexHandler.matches(ex.getData(), RegexStrings.VARIABLE_NAME+"|"+
//                RegexStrings.DOUBLE+"|"+RegexStrings.INTEGER+"|"+"true\\|false"))
//            throw new IllegalExpression("cannot resolve "+ex.getData(), lineNumber);

        return ex.getData();
    }

    private static LinkedList<String> parseParameters() throws UnexpectedToken {
        if (getNextNotWhiteSpace().type != TokenType.OPENPAR)
            throw new UnexpectedToken("missing open par", lineNumber);

        Token paramType = getNextNotWhiteSpace();
        if (paramType.type == TokenType.CLOSEPAR) {
            return null;
        }
        Token lastToken;
        LinkedList<String> parameters = new LinkedList<>();
        parameters.add(buildParameter(paramType));
        while((lastToken=getNextNotWhiteSpace()).type==TokenType.COMMA) {
            parameters.add(buildParameter());
        }

        if (lastToken.type!=TokenType.CLOSEPAR)
            throw new UnexpectedToken("missing closepar", lineNumber);
        return parameters;
    }

    private static LinkedList<String> parseMethodCallParameters() throws UnexpectedToken {
        if (getNextNotWhiteSpace().type != TokenType.OPENPAR)
            throw new UnexpectedToken("missing open par", lineNumber);

        Token param = getNextNotWhiteSpace();
        if (param.type == TokenType.CLOSEPAR) {
            return null;
        }
        Token lastToken;
        LinkedList<String> parameters = new LinkedList<>();
        parameters.add(buildMethodCallParameter(param));
        while((lastToken=getNextNotWhiteSpace()).type==TokenType.COMMA) {
            parameters.add(buildMethodCallParameter());
        }

        if (lastToken.type!=TokenType.CLOSEPAR)
            throw new UnexpectedToken("missing closepar", lineNumber);
        return parameters;
    }

    private static String buildMethodCallParameter(Token param) throws UnexpectedToken {
        if (param.type != TokenType.UNRECOGNIZED)
            throw new UnexpectedToken(param.getData(), lineNumber);
        return param.getData();
    }

    private static String buildMethodCallParameter() throws UnexpectedToken {
        Token param = getNextNotWhiteSpace();
        return buildMethodCallParameter(param);
    }


    private static String buildParameter(Token paramType) throws UnexpectedToken {
        String parameter = "";
        if (!(paramType.type == TokenType.VARIABLE))
//            if (paramType.getData().equals("final")) {
            if (paramType.type==TokenType.FINAL) {
                parameter += paramType.getData() + " ";
                checkNextWhiteSpace();
                paramType=getNextExceptedToken();
            }
            else throw new UnexpectedToken(paramType.getData(), lineNumber);
        parameter+= paramType.getData();
        Token name = getNextNotWhiteSpace();
        //reserved words&chars
        if (name.type!=TokenType.UNRECOGNIZED)
            throw new UnexpectedToken(name.getData(), lineNumber);
//        if (!RegexHandler.matches(name, "(?:_\\w+|[a-zA-Z]+\\w*)"))
//            throw new IllegalVariableName(lineNumber);
        return parameter+" "+name.getData();
    }

    private static String buildParameter() throws UnexpectedToken {
        Token paramType = getNextNotWhiteSpace();
        return buildParameter(paramType);
    }

    private static Token getNextNotWhiteSpace() throws UnexpectedToken {
        Token checkedToken = getNextExceptedToken();
        if (checkedToken.type == TokenType.WHITESPACE)
            return getNextExceptedToken();
        return checkedToken;
    }

    private static String parseReturnType() throws UnexpectedToken {
        String returnType = getNextExceptedToken().getData();
        checkNextWhiteSpace();
        return returnType;
    }

    private static Token getNextToken(){
        if (tokenIterator.hasNext())
            return tokenIterator.next();
        return null;
    }

    private static Token getNextExceptedToken() throws UnexpectedToken {
        if (tokenIterator.hasNext())
            return tokenIterator.next();
        throw new UnexpectedToken(lineNumber);
    }

    private static String parseMethodName() throws UnexpectedToken {
        Token methodName = getNextExceptedToken();
        //reserved words&chars
        if (methodName.type!=TokenType.UNRECOGNIZED)
            throw new UnexpectedToken(methodName.getData(), lineNumber);
//        if (!RegexHandler.matches(methodName, "[a-zA-Z]+\\w*"))
//            throw new IllegalMethodName(methodName, lineNumber);
        return methodName.getData();
    }

    private static void checkNextWhiteSpace() throws UnexpectedToken {
        Token checkedToken = getNextExceptedToken();
        if (checkedToken.type != TokenType.WHITESPACE)
            throw new UnexpectedToken(checkedToken.getData(), lineNumber);
    }

    private static void checkSuffix(char suffix) throws UnexpectedToken {
        Token checkedToken = getNextNotWhiteSpace();
        if (checkedToken.type != TokenType.SUFFIX || checkedToken.getData().charAt(0)!=suffix)
            throw new UnexpectedToken(checkedToken.getData(), lineNumber);
    }
}
