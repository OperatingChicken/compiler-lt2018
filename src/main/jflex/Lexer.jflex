import err.LexError;
import java.io.Reader;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.Symbol;

%%

%apiprivate
%cup
%line
%column
%class Lexer
%yylexthrow LexError
%state BODY, END

%{
    private ComplexSymbolFactory sf;
    public Lexer(Reader in, ComplexSymbolFactory sf) {
	    this(in);
	    this.sf = sf;
    }
    private Symbol makeSym(String name, int id, Object value) {
        Location location = new Location(yyline + 1, yycolumn + 1);
        return sf.newSymbol(name, id, location, location, value);
    }
    private Symbol makeSym(String name, int id) {
        return makeSym(name, id, null);
    }
%}

Space = [ ] | \t | \f
NewLine = \n | \r | \r\n
Comment = "//".*{NewLine}
CommentsOrNewLines = ({Comment}|{NewLine}|" ")+
Identifier = [:jletter:][:jletterdigit:]*
DecLiteral = [:digit:]+
HexLiteral = 0x[0-9a-fA-F]+
StringLiteral = \".*\"

%%
<YYINITIAL> {
{CommentsOrNewLines} { }
"" {yybegin(BODY);}
}
<BODY> {
    {Space} { }
    "&"{CommentsOrNewLines} { }
    {CommentsOrNewLines} {return makeSym("NEWLINE", ParserSym.NEWLINE);}
    "input" {return makeSym("INPUT", ParserSym.INPUT);}
    "output" {return makeSym("OUTPUT", ParserSym.OUTPUT);}
    "newLine" {return makeSym("OUTPUT_NEWLINE", ParserSym.OUTPUT_NEWLINE);}
    "loop" {return makeSym("LOOP", ParserSym.LOOP);}
    "endLoop" {return makeSym("END_LOOP", ParserSym.END_LOOP);}
    "+" {return makeSym("ADD", ParserSym.ADD);}
    "-" {return makeSym("SUB", ParserSym.SUB);}
    "*" {return makeSym("MUL", ParserSym.MUL);}
    "/" {return makeSym("DIV", ParserSym.DIV);}
    "%" {return makeSym("MOD", ParserSym.MOD);}
    "=" {return makeSym("EQUALS", ParserSym.EQUALS);}
    "?" {return makeSym("QUESTION_MARK", ParserSym.QUESTION_MARK);}
    ":" {return makeSym("COLON", ParserSym.COLON);}
    "(" {return makeSym("LPAREN", ParserSym.LPAREN);}
    ")" {return makeSym("RPAREN", ParserSym.RPAREN);}
    {Identifier} {return makeSym("IDENTIFIER", ParserSym.IDENTIFIER, yytext());}
    {DecLiteral} {return makeSym("NUM_LITERAL", ParserSym.NUM_LITERAL, Long.parseLong(yytext()));}
    {HexLiteral} {return makeSym("NUM_LITERAL", ParserSym.NUM_LITERAL, Long.decode(yytext()));}
    {StringLiteral} {return makeSym("STRING_LITERAL", ParserSym.STRING_LITERAL, yytext().substring(1, yylength() - 1));}
    [^] {throw new LexError(yyline + 1, yycolumn + 1, yytext());}
}
<<EOF>> {return makeSym("End of file", ParserSym.EOF);}