package sjava.verifier.main;

import java.io.File;

public class tester {

    public static void main(String[] args){
        File testsdir = new File("C:\\Users\\Damien\\Desktop\\Semester 2\\OOP\\ex6\\src\\tests");
        for (File test: testsdir.listFiles()) {
            String[] arg = {test.getAbsolutePath()};

            boolean ToTest = true;
//            boolean ToTest = false;
            if (test.getName().equals("test257.sjava"))
                ToTest=true;
            if (ToTest){
                System.out.println("Test "+test.getName()+": ");
                Sjavac.main(arg);
                System.out.println("test completed.");
            }
        }
        System.out.println("**** TESTS COMPLETES. ****");
    }
}
