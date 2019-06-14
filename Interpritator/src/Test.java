import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("src/Test.txt");
        List<Token> tokens = lexer.go_lex();

        System.out.println("lexem:tag:priority");
        for (Token token : tokens){
            System.out.println(token.getValue() + " " + token.getTag() + " " + token.getPriority());
        }

        Parser parser = new Parser(tokens);
        if (parser.lang()){
            System.out.println("\nTest passed!");
        }else{
            System.out.println("\nTest failed!");
        }
        System.out.println("Final POLIZ:");
        for (String p : parser.getPoliz()){
            System.out.print(p + " ");
        }

        StackMachine stackMachine = new StackMachine(parser.getPoliz());
        stackMachine.execute();
        System.out.println("\n\nVariables:");
        stackMachine.printVars();

    }
}
