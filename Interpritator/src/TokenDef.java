import java.util.ArrayList;
import java.util.List;

public class TokenDef{

    public static List<Lexem> tokensDef;

    public TokenDef(){
        tokensDef = new ArrayList<>();
        tokensDef.add(new Lexem("NONE", "^/\\*.*\\*/", 0)); //Коммент
        tokensDef.add(new Lexem("NONE", "^[ \t\n]+", 0));  //Пробелы, табы, новые строки
        tokensDef.add(new Lexem("IF_KW", "^if", 1));
        tokensDef.add(new Lexem("WHILE_KW", "^while", 1));
        tokensDef.add(new Lexem("VAR", "^[A-Za-z_][A-Za-z0-9_]*", 0));
        tokensDef.add(new Lexem("FLOAT", "^-?[0-9]+\\.[0-9]+", 0));
        tokensDef.add(new Lexem("INT", "^-?[1-9][0-9]*", 0));
        tokensDef.add(new Lexem("BRACKET_OPEN", "^\\(", 0));
        tokensDef.add(new Lexem("BRACKET_CLOSE", "^\\)", 0));
        tokensDef.add(new Lexem("FBRACKET_OPEN", "^\\{", 0));
        tokensDef.add(new Lexem("FBRACKET_CLOSE", "^\\}", 0));
        tokensDef.add(new Lexem("OP_PLUS", "^\\+", 6));
        tokensDef.add(new Lexem("OP_MINUS", "^-", 6));
        tokensDef.add(new Lexem("OP_MUL", "^\\*", 7));
        tokensDef.add(new Lexem("OP_DIV", "^/", 7));
        tokensDef.add(new Lexem("COP_NEQ", "^!=", 4));
        tokensDef.add(new Lexem("COP_EQ", "^==", 4));
        tokensDef.add(new Lexem("COP_MOR", "^>", 5));
        tokensDef.add(new Lexem("COP_LES", "^<", 5));
        tokensDef.add(new Lexem("COP_EQMOR", "^>=", 5));
        tokensDef.add(new Lexem("COP_EQLES", "^<=", 5));
        tokensDef.add(new Lexem("ASSIGN_OP", "^=", 1));
        tokensDef.add(new Lexem("SEMICOLON", "^;", 0));
    }

    public List<Lexem> getTokensDef() {
        return tokensDef;
    }

}
