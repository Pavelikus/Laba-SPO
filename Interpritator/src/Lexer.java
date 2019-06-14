import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Lexer {

    private StringBuilder input = new StringBuilder();
    private List<Lexem> lexems = new TokenDef().getTokensDef();

    public Lexer(String path){
        try (Stream<String> st = Files.lines(Paths.get(path))) {
            st.forEach(input::append);
        } catch (IOException ex) {
            ex.fillInStackTrace();
            return;
        }
    }

    public List<Token> go_lex(){
        int pos = 0;
        List<Token> tokens = new ArrayList<>();
        while(pos < this.input.length()){
            Boolean match = false;
            Matcher matcher;
            Pattern pattern;
            for (Lexem lexem_def : this.lexems){
                pattern = Pattern.compile(lexem_def.getRegexp());
                matcher = pattern.matcher(input.substring(pos));
                match = matcher.find();
                if (match){
                    if (!lexem_def.getName().equals("NONE")) {
                        tokens.add(new Token(matcher.group(), lexem_def.getName(), lexem_def.getPriority()));
                        pos += matcher.end();
                    }
                    else{
                        pos += matcher.end();
                    }
                    break;
                }
            }
            if (!match) {
                System.out.println("Wrong character : " + input.toString().charAt(pos));
                return tokens;
            }
        }
        return tokens;
    }
}
