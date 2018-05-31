import java_cup.runtime.*;

%%

//%cup
%type String
%line
%column
%class Lexer

Space = [ ] | \t | \f
NewLine = \n | \r | \r\n
Comment = "//".*{NewLine}
Identifier = [:jletter:][:jletterdigit:]*
DecLiteral = [:digit:]
HexLiteral = 0x[0-9a-fA-F]+
StringLiteral = \".*\"

%%
{Space} { }
{Comment} {return "NEWLINE";}
{NewLine} {return "NEWLINE";}
"input" {return "INPUT";}
"output" {return "OUTPUT";}
"newLine" {return "OUTPUT_NEWLINE";}
"loop" {return "LOOP";}
"endLoop" {return "END_LOOP";}
"+" {return "ADD";}
"-" {return "SUB";}
"*" {return "MUL";}
"/" {return "DIV";}
"%" {return "MOD";}
"=" {return "EQUALS";}
"?" {return "QUESTION_MARK";}
":" {return "COLON";}
{Identifier} {return "IDENTIFIER";}
{DecLiteral} {return "DEC_LITERAL";}
{HexLiteral} {return "HEX_LITERAL";}
{StringLiteral} {return "STRING_LITERAL";}
