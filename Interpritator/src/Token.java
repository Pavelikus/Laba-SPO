public class Token {

    private String value;
    private String tag;
    private int priority;

    public Token(String value, String tag, int priority){
        this.value = value;
        this.tag = tag;
        this.priority = priority;
    }

    public String getValue() {
        return value;
    }

    public String getTag() {
        return tag;
    }

    public int getPriority() {
        return priority;
    }
}
