import ast.statements.Stmt;
import java_cup.runtime.ComplexSymbolFactory;

import java.io.FileReader;
import java.io.Reader;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            //System.err.println("Usage: java -jar compiler-lt2018.jar inputFileName");
            //System.exit(1);
            args = new String[]{"default.lang"};
        }
        Reader in = new FileReader(args[0]);
        ComplexSymbolFactory symbolFactory = new ComplexSymbolFactory();
        Lexer lexer = new Lexer(in, symbolFactory);

        /*Symbol sym;
        while ((sym = lexer.next_token()).sym != ParserSym.EOF) {
            System.out.println(sym);
        }*/

        Parser parser = new Parser(lexer, symbolFactory);
        Stmt astRoot = (Stmt) parser.parse().value;
        System.out.println(astRoot.toString());
    }
}
