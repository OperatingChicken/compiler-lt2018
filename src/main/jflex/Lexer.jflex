import java.io.Reader;
import java_cup.runtime.ComplexSymbolFactory;

%%

%cup
%line
%column
%class Lexer

%{
    private ComplexSymbolFactory sf;
    public Lexer(Reader in, ComplexSymbolFactory sf) {
	    this(in);
	    this.sf = sf;
    }
%}

Space = [ ] | \t | \f
NewLine = \n | \r | \r\n
Comment = "//".*{NewLine}
Identifier = [:jletter:][:jletterdigit:]*
DecLiteral = [:digit:]+
HexLiteral = 0x[0-9a-fA-F]+
StringLiteral = \".*\"

%%
{Space} { }
//{Comment} {return sf.newSymbol("NEWLINE", ParserSym.NEWLINE);}
({Comment}|{NewLine})+ {return sf.newSymbol("NEWLINE", ParserSym.NEWLINE);}
"input" {return sf.newSymbol("INPUT", ParserSym.INPUT);}
"output" {return sf.newSymbol("OUTPUT", ParserSym.OUTPUT);}
"newLine" {return sf.newSymbol("OUTPUT_NEWLINE", ParserSym.OUTPUT_NEWLINE);}
"loop" {return sf.newSymbol("LOOP", ParserSym.LOOP);}
"endLoop" {return sf.newSymbol("END_LOOP", ParserSym.END_LOOP);}
"+" {return sf.newSymbol("ADD", ParserSym.ADD);}
"-" {return sf.newSymbol("SUB", ParserSym.SUB);}
"*" {return sf.newSymbol("MUL", ParserSym.MUL);}
"/" {return sf.newSymbol("DIV", ParserSym.DIV);}
"%" {return sf.newSymbol("MOD", ParserSym.MOD);}
"=" {return sf.newSymbol("EQUALS", ParserSym.EQUALS);}
"?" {return sf.newSymbol("QUESTION_MARK", ParserSym.QUESTION_MARK);}
":" {return sf.newSymbol("COLON", ParserSym.COLON);}
"(" {return sf.newSymbol("LPAREN", ParserSym.LPAREN);}
")" {return sf.newSymbol("RPAREN", ParserSym.RPAREN);}
"&" {return sf.newSymbol("LINEWRAP", ParserSym.LINEWRAP);}
{Identifier} {return sf.newSymbol("IDENTIFIER", ParserSym.IDENTIFIER, yytext());}
{DecLiteral} {return sf.newSymbol("NUM_LITERAL", ParserSym.NUM_LITERAL, Long.parseLong(yytext()));}
{HexLiteral} {return sf.newSymbol("NUM_LITERAL", ParserSym.NUM_LITERAL);}
{StringLiteral} {return sf.newSymbol("STRING_LITERAL", ParserSym.STRING_LITERAL, yytext().substring(1, yylength() - 1));}
<<EOF>> {return sf.newSymbol("End of file", ParserSym.EOF);}