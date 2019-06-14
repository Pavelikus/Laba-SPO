
public class Lexem {
    private String name;
    private String regexp;
    private int priority;

    public String getName() {
        return name;
    }

    public String getRegexp() {
        return regexp;
    }

    public int getPriority() {
        return priority;
    }

    Lexem(String n, String r, int p) {
        name=n;
        regexp=r;
        priority=p;
   }

}