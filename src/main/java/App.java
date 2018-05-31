import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
		System.err.println("Usage: java -jar compiler-lt2018.jar inputFileName");
		System.exit(1);
	}
        Reader in = new FileReader(args[0]);
        Lexer lexer = new Lexer(in);
        String tok;
        while ((tok = lexer.yylex()) != null) {
            System.out.println(tok + " " + lexer.yytext().replace("\n", "\\n"));
        }
    }
}
