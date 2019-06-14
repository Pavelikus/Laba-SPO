import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {

    private List<Token> tokens;
    private int pos;
    private List<String> poliz;
    private List<Token> buffer;
    private List<Integer> addrsForFilling;
    private List<Integer> addrsJumps;
    private List<String> calls;


    public Parser(List<Token> tokens){
        this.tokens = tokens;
        poliz = new ArrayList<String>();
        buffer = new ArrayList<Token>();
        addrsForFilling = new ArrayList<>(); //адреса, куда записываются адреса переходов
        addrsJumps = new ArrayList<>(); // адреса переходов в начало условия
        calls = new ArrayList<>(); // стек вызовов операций while, if
        this.pos = 0;
    }

    public List<String> getPoliz(){
        return this.poliz;
    }

    public boolean lang(){
        while(this.pos < this.tokens.size()){
            if (!this.expr()) {
                this.printExeption(this.tokens.get(this.pos).getValue(), "Expression");
                return false;
            }
        }
        return true;
    }

    //expr -> init | if_stmt | loopwhile
    private boolean expr() {
        if (!(this.init() || this.if_stmt() || this.loopwhile()))
            return false;
        return true;
    }

    // init -> VAR ASSIGN_OP stmt
    private boolean init() {
        if (!(this.var())) {
            return false;
        } else if (!(this.assign_op())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "=");
            return false;
        } else if (!(this.stmt())) {
            return false;
        } else if (!(this.semicolon())) {
            return false;
        }
        return true;
    }

    private boolean semicolon(){
        if(this.tokens.get(this.pos).getTag().equals("SEMICOLON")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean var(){
        if(this.tokens.get(this.pos).getTag().equals("VAR")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean assign_op() {
        if(this.tokens.get(this.pos).getTag().equals("ASSIGN_OP")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    //stmt -> value(OP value)*
    private boolean stmt() {
        if (this.value()) {
            while (true) {
                if (this.op()) {
                    if (!(this.value())) {
                        this.printExeption(this.tokens.get(this.pos).getValue(), "Value");
                        return false;
                    }
                } else break;
            }
        }
        return true;
    }

    // value -> VAR | DIGIT | | BCKEXPR
    private boolean value() {
        if (!(this.var() || this.digit() || this.brcktexpr()))
            return false;
        return true;
    }

    private boolean brcktexpr() {
        if (!(this.bracket_open())) {
            return false;
        } else if (!(this.stmt())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Statement");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        }
        return true;
    }

    private boolean digit() {
        if(this.tokens.get(this.pos).getTag().equals("INT") ||
           this.tokens.get(this.pos).getTag().equals("FLOAT")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    // if_stmt -> IF_KW BRACKET_OPEN comp BRACKET_CLOSE FBRACKET_OPEN stmt* FBRACKET_CLOSE
    private boolean if_stmt() {
        if (!(this.if_kw())) {
            return false;
        } else if (!(this.bracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "(");
            return false;
        } else if (!(this.comp())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Comparison");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        } else if (!(this.fbracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "{");
            return false;
        } else {
            while (true) {
                if (!(this.expr())) {
                    break;
                }
            }
        }
        if (!(this.fbracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "}");
            return false;
        }
        return true;
    }

    private boolean if_kw() {
        if(this.tokens.get(this.pos).getTag().equals("IF_KW") ) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean bracket_open() {
        if(this.tokens.get(this.pos).getTag().equals("BRACKET_OPEN")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean bracket_close() {
        if(this.tokens.get(this.pos).getTag().equals("BRACKET_CLOSE")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean fbracket_open() {
        if(this.tokens.get(this.pos).getTag().equals("FBRACKET_OPEN")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean fbracket_close() {
        if(this.tokens.get(this.pos).getTag().equals("FBRACKET_CLOSE")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private boolean op() {
        if(this.tokens.get(this.pos).getTag().equals("OP_PLUS")
                || this.tokens.get(this.pos).getTag().equals("OP_MINUS")
                || this.tokens.get(this.pos).getTag().equals("OP_MUL")
                || this.tokens.get(this.pos).getTag().equals("OP_DIV")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    //comp -> stmt COMPARE_OP stmt
    private boolean comp() {
        if (!(this.stmt())) {
            return false;
        } else if (!(this.compare_op())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Compare operation");
            return false;
        } else if (!(this.stmt())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Statement");
            return false;
        }
        return true;
    }

    private boolean compare_op() {
        if(this.tokens.get(this.pos).getTag().equals("COP_NEQ")
                || this.tokens.get(this.pos).getTag().equals("COP_EQ")
                || this.tokens.get(this.pos).getTag().equals("COP_MOR")
                || this.tokens.get(this.pos).getTag().equals("COP_LES")
                || this.tokens.get(this.pos).getTag().equals("COP_EQMOR")
                || this.tokens.get(this.pos).getTag().equals("COP_EQLES")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    // loopwhile -> WHILE_KW BRACKET_OPEN comp BRACKET_CLOSE FBRACKET_OPEN expr* FBRACKET_CLOSE
    private boolean loopwhile() {
        if (!(this.while_kw())) {
            return false;
        } else if (!(this.bracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "(");
            return false;
        } else if (!(this.comp())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "Comparison");
            return false;
        } else if (!(this.bracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), ")");
            return false;
        } else if (!(this.fbracket_open())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "{");
            return false;
        } else {
            while (true) {
                if (!(this.expr())) {
                    break;
                }
            }
        }
        if (!(this.fbracket_close())) {
            this.printExeption(this.tokens.get(this.pos).getValue(), "}");
            return false;
        }
        return true;
    }

    private boolean while_kw() {
        if(this.tokens.get(this.pos).getTag().equals("WHILE_KW")) {
            this.push(this.tokens.get(this.pos++));
            return true;
        }
        return false;
    }

    private Token endEl(List<Token> list){
        if (list.size() != 0)
            return list.get(list.size() - 1);
        else
            return null;
    }

    private void push(Token token){
        if (token.getTag().equals("VAR") ||
            token.getTag().equals("INT") ||
            token.getTag().equals("FLOAT")
        ){
            this.poliz.add(token.getValue());
        } else {
            if (token.getValue().equals("while") || token.getValue().equals("if")){
                this.calls.add(token.getValue());
                this.buffer.add(token);
                if(token.getValue().equals("while"))
                    this.addrsJumps.add(this.poliz.size());
            }
            else if (token.getValue().equals(")")){
                while(!this.buffer.get(this.buffer.size() - 1).getValue().equals("(")){
                    poliz.add(buffer.remove(buffer.size() - 1).getValue());
                }
                buffer.remove(buffer.size() - 1);
                if (this.buffer.get(this.buffer.size() - 1).getValue().equals("while") ||
                        this.buffer.get(this.buffer.size() - 1).getValue().equals("if")){
                    this.buffer.remove(this.buffer.size() - 1);
                    this.addrsForFilling.add(this.poliz.size());
                    this.poliz.add(""); //rewrite later
                    this.poliz.add("!F");
                }
                return;
            } else if (token.getValue().equals("}")) {
                while (!this.endEl(this.buffer).getValue().equals("{")) {
                    poliz.add(buffer.remove(buffer.size() - 1).getValue());
                }
                buffer.remove(buffer.size() - 1);
                String lastCall = calls.remove(calls.size() - 1);
                if (lastCall.equals("while")) {
                    this.poliz.add(this.addrsJumps.remove(this.addrsJumps.size() - 1).toString());
                    this.poliz.add("!");
                    int buf = this.addrsForFilling.remove(this.addrsForFilling.size() - 1);
                    this.poliz.set(
                            buf,
                            String.valueOf(this.poliz.size())
                    );
                } else {
                    int buf = this.addrsForFilling.remove(this.addrsForFilling.size() - 1);
                    this.poliz.set(
                            buf,
                            String.valueOf(this.poliz.size())
                    );
                }
                return;
            } else if (!token.getValue().equals("(") && !token.getValue().equals("{") && buffer.size() != 0){
                if (token.getPriority() < this.buffer.get(this.buffer.size() - 1).getPriority()) {
                    if (token.getValue().equals(";"))
                        while (!this.buffer.get(this.buffer.size() - 1).getValue().equals("="))
                            this.poliz.add(buffer.remove(this.buffer.size() - 1).getValue());
                    this.poliz.add(buffer.remove(this.buffer.size() - 1).getValue());
                }
            }
            if (!token.getValue().equals(";"))
                this.buffer.add(token);
        }
    }

    private void printExeption(String detected, String expected){
        System.out.println("\nParse error: detected " + "'" + detected +
                "', but " + "'" + expected + "' are expected!");
        System.exit(0);
    }

}

