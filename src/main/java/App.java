import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

import java.io.Reader;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            //System.err.println("Usage: java -jar compiler-lt2018.jar inputFileName");
            //System.exit(1);
            args = new String[]{"default"};
        }
        Reader in = new FileReader(args[0]);
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
        Lexer lexer = new Lexer(in, symbolFactory);

        /*Symbol sym;
        while ((sym = lexer.next_token()).sym != ParserSym.EOF) {
            System.out.println(sym);
        }*/

        Parser parser = new Parser(lexer, symbolFactory);
        Symbol astRoot = parser.parse();
    }
}
