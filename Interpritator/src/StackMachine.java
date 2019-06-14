import java.io.SyncFailedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StackMachine {
    private int pos;
    private List<String> poliz;
    private List<Object> stack;
    private HashMap<Object, Object> variables;

    public StackMachine(List<String> poliz){
        this.poliz = poliz;
        this.stack = new ArrayList<>();
        this.pos = 0;
        this.variables = new HashMap<>();
    }

    public void printVars(){
        for (Object key : variables.keySet()){
            System.out.println(key.toString() + " = " + variables.get(key).toString());
        }
    }

    public void execute(){
        while (this.pos < this.poliz.size()){
            this.stack.add(this.poliz.get(this.pos++));
            String stackHead = this.stack.remove(this.stack.size() - 1).toString();

            if (stackHead.equals("!")){
                this.pos = Integer.valueOf(this.stack.remove(this.stack.size() - 1).toString());
            } else if (stackHead.equals("!F")){
                int addr = Integer.valueOf(this.stack.remove(this.stack.size() - 1).toString());
                if (!Boolean.valueOf(this.stack.remove(this.stack.size() - 1).toString()))
                    this.pos = addr;
            } else if (stackHead.equals("*") || stackHead.equals("/") || stackHead.equals("=") ||
                    stackHead.equals("+") || stackHead.equals("-") || stackHead.equals(">") ||
                    stackHead.equals("<") || stackHead.equals("==") || stackHead.equals("!=") ||
                    stackHead.equals("<=") || stackHead.equals(">=")){
                this.calculate(stackHead);
            } else
                this.stack.add(stackHead);
        }
    }

    private Object check (Object a) {
        if (variables.get(a) != null) {
            a = variables.get(a);
        }
        return a;
    }

    private void checkDef(Object a){
        if (!a.equals("true") || !a.equals("false")) { // проверка на объявление переменной
            try {
                float buf = Float.valueOf(a.toString());
            } catch (Exception e){
                System.out.println("\nПеременная не объявлена: " + a);
                System.exit(0);
                e.fillInStackTrace();
            }
        }
    }

    private void calculate(String op){

        Object b = this.stack.remove(this.stack.size() - 1);
        Object a = this.stack.remove(this.stack.size() - 1);


        b = check(b);
        if (op.equals("=")) {
            this.assign(a, b);
            return;
        }
        a = check(a);

        checkDef(a);
        checkDef(b);

        switch (op){
            case "*": {
                this.mul(a, b);
                break;
            }
            case "-": {
                this.minus(a, b);
                break;
            }
            case "+": {
                this.plus(a, b);
                break;
            }
            case "/": {
                this.div(a, b);
                break;
            }
            case ">": {
                this.greater(a, b);
                break;
            }
            case ">=": {
                this.greater_eq(a, b);
                break;
            }
            case "<": {
                this.less(a, b);
                break;
            }
            case "<=": {
                this.less_eq(a, b);
                break;
            }
            case "==": {
                this.equal(a, b);
                break;
            }
            case "!=": {
                this.not_equal(a, b);
                break;
            }
        }

    }

    private void assign(Object a, Object b){
        if (variables.get(b) != null) {
            b = variables.get(b);
        }
        variables.put(a,b);
    }

    private void mul(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) * Float.valueOf(b.toString()));
    }

    private void minus(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) - Float.valueOf(b.toString()));
    }

    private void greater(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) > Float.valueOf(b.toString()));
    }

    private void greater_eq(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) >= Float.valueOf(b.toString()));
    }

    private void less(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) < Float.valueOf(b.toString()));
    }

    private void less_eq(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) <= Float.valueOf(b.toString()));
    }

    private void div(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) / Float.valueOf(b.toString()));
    }

    private void plus(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) + Float.valueOf(b.toString()));
    }

    private void equal(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) == Float.valueOf(b.toString()));
    }

    private void not_equal(Object a, Object b) {
        this.stack.add(Float.valueOf(a.toString()) != Float.valueOf(b.toString()));
    }
}
