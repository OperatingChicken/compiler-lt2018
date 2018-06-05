import java_cup.runtime.ComplexSymbolFactory;

import java.io.Reader;
import java.io.FileReader;
import java.util.ArrayList;

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
        ArrayList<Stmt> stmtList = (ArrayList<Stmt>) parser.parse().value;

        for (Stmt stmt: stmtList) {
            System.out.println(stmt.toString());
        }
    }
}
