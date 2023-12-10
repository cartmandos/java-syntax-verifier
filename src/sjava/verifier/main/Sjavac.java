package sjava.verifier.main;

import sjava.verifier.parser.*;
import sjava.verifier.scope.ClassScope;
import sjava.verifier.scope.Scope;
import sjava.verifier.scope.ScopeException;
import sjava.verifier.variable.VariableException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sjavac {


    private static final int NUMBER_OF_ARGS = 1;
    private static final String WRONG_ARG_NUM = "Wrong number of arguments.";
    private static final int FILE_SOURCE = 0;
    private static final String SJAVA_FILE_EXTENSION = ".sjava";
    private static final String FILE_NOT_FOUND = "File not found.";
    private static final String INVALID_ARGUMENT_TYPE = "File is not a sJava file.";
    private static final String IO_ERROR = "I/O error occurred.";
    private static final int LEGAL_CODE_OUTPUT = 0;
    private static final int ILLEGAL_CODE_OUTPUT = 1;
    private static final int IO_ERROR_OUTPUT = 2;
    private static int verifierOutput;
    private static String logString;

    public static void main(String args[]){
        try {

            File sJavaFile = loadFile(getFileSource(args));
            Scope sJavaScope = sJavaParser.parseSJava(sJavaFile);

            //region testers
//            System.out.println(sJavaScope);
            //endregion

            AnalyzeCode.analyzeScope((ClassScope) sJavaScope);
            setOutput(LEGAL_CODE_OUTPUT);
        } catch (ParseException | ScopeException | VariableException | IllegalArgumentException e) {
            setOutput(ILLEGAL_CODE_OUTPUT, e.getMessage());
        } catch (IOException e) {
            if ("".equals(e.getMessage())) // not good
                setOutput(IO_ERROR_OUTPUT, IO_ERROR);
            else setOutput(IO_ERROR_OUTPUT, e.getMessage());
        } finally {
            verifierOutput();
        }
    }

    private static File loadFile(String fileSource) throws IllegalArgumentException, FileNotFoundException {
        File sJavaFile = new File(fileSource);
        if (!sJavaFile.exists() || !sJavaFile.isFile())
            throw new FileNotFoundException(FILE_NOT_FOUND);
        else if (!fileSource.endsWith(SJAVA_FILE_EXTENSION))
            throw new IllegalArgumentException(INVALID_ARGUMENT_TYPE);
        return sJavaFile;
    }

    private static String getFileSource(String[] args) throws IllegalArgumentException {
        if (args.length != NUMBER_OF_ARGS)
            throw new IllegalArgumentException(WRONG_ARG_NUM);
        return args[FILE_SOURCE];
    }

    private static void setOutput(int result){
        verifierOutput = result;
    }

    private static void setOutput(int result, String error){
        verifierOutput = result;
        logError(error);
    }

    private static void verifierOutput(){
        if (logString != null)
            errorPrinter(logString);
        System.out.println(verifierOutput);
    }

    private static void errorPrinter(String errorMessage){
        System.err.println(errorMessage);
    }

    private static void logError(String error){
        logString = error;
    }
}
