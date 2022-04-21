/*-***
 * This grammar is defined for the example grammar defined in the
 *project part 1 instructions
 */

/*
 * NOTE: make sure that the java cup runtime file is in the same directory as this file
 * it is also alright if the runtime location is added to to the classpath, but just
 * putting in the same file is far easier.
 */
import java_cup.runtime.*;


%%
/*-*
 * LEXICAL FUNCTIONS:
 */

%cup
%line
%column
%unicode
%class Scanner
/*
 * NOTE: the above name ExampleLexer, will have to be changed here if
 * you chose to rename the lexer object.
 */
 
%{

/**
 * Return a new Symbol with the given token id, and with the current line and
 * column numbers.
 */
Symbol newSym(int tokenId) {
    return new Symbol(tokenId, yyline, yycolumn);
}

/**
 * Return a new Symbol with the given token id, the current line and column
 * numbers, and the given token value.  The value is used for tokens such as
 * identifiers and numbers.
 */
Symbol newSym(int tokenId, Object value) {
    return new Symbol(tokenId, yyline, yycolumn, value);
}

%}


/*-*
 * PATTERN DEFINITIONS:
 */

tab           = \\t
newline         = \\n
slash = \\
letter  = [A-Za-z]
digit   = [0-9]
id  = {letter}[{letter}{digit}]*

intlit  = {digit}+
floatlit = {intlit}\.{intlit}
char = [^\\\n\t\"\']|\\.
str = {char}*
charlit = \'{char}\'
escapequote     = {slash}\"
stringchar      = [[[^\\]&&[^\"]]&&[[^\n]&&[^\t]]]|{newline}|{tab}|{escapequote}|{slash}{slash}
stringlit       = \"{stringchar}*\"

comment = {slash}{slash}.*\n
multi_comment = {slash}\*(\\[^\*]|[^\\])*\*{slash}
whitespace = [ \n\t\r]



%%
/**
 * LEXICAL RULES:
 */
class           { return newSym(sym.CLASS, "class"); }
read            { return newSym(sym.READ, "read"); }
print       { return newSym(sym.PRINT, "print"); }
printline       { return newSym(sym.PRINTLINE, "println"); }
final           { return newSym(sym.FINAL, "final"); }
int          { return newSym(sym.INT, "int"); }
char         { return newSym(sym.CHAR, "char"); }
bool            { return newSym(sym.BOOL, "bool"); }
float        { return newSym(sym.FLOAT, "float"); }
void            { return newSym(sym.VOID, "void"); }
if     { return newSym(sym.IF, "if"); }
while           { return newSym(sym.WHILE, "while"); }
return          { return newSym(sym.RETURN, "return"); }
else            { return newSym(sym.ELSE, "else"); }
true            { return newSym(sym.TRUE, "true"); }
false           { return newSym(sym.FALSE, "false"); }
"("             { return newSym(sym.LEFTBR, "left bracket"); }
"{"             { return newSym(sym.LEFTCBR, "left curly bracket"); }
"["             { return newSym(sym.LEFTSBR, "left square bracket"); }
")"             { return newSym(sym.RIGHTBR, "right bracket"); }
"}"             { return newSym(sym.RIGHTCBR, "right curly bracket"); }
"]"             { return newSym(sym.RIGHTSBR, "right square bracket"); }
"*"             { return newSym(sym.TIMES, "*"); }
"+"             { return newSym(sym.PLUS, "+"); }
"-"             { return newSym(sym.MINUS, "-"); }
"/"             { return newSym(sym.DIVIDE, "/"); }
"="             { return newSym(sym.ASSIGN, "="); }
";"             { return newSym(sym.SEMI, ";"); }
","               { return newSym(sym.COM, ","); }
"<"               { return newSym(sym.LT, "< = less than"); }
">"               { return newSym(sym.GT, "> - greater than"); }
"<="               { return newSym(sym.LEQ, "<= - less than or equal"); }
">="               { return newSym(sym.GEQ, ">= - greater than or equal"); }
"=="               { return newSym(sym.EEQ, "=="); }
"<>"               { return newSym(sym.NEQ, "<> - not equal to"); }
":"               { return newSym(sym.COL, ":"); }
"||"               { return newSym(sym.OR, "OR"); }
"&&"               { return newSym(sym.AND, "AND"); }
"++"               { return newSym(sym.INC, "++ - increment"); }
"--"               { return newSym(sym.DEC, "-- - decrement"); }
"~"               { return newSym(sym.NOT, "~"); }
"?"               { return newSym(sym.QUES, "?"); }
{id}               { return newSym(sym.ID, yytext()); }
{intlit}           { return newSym(sym.INTLIT, new String(yytext())); }
{floatlit}           { return newSym(sym.FLOATLIT, new String(yytext())); }
{stringlit}           { return newSym(sym.STRINGLIT, new String(yytext())); }
{whitespace}       { /* Ignore whitespace. */ }
{comment}           {}
{charlit}           { return newSym(sym.CHARLIT, new String(yytext())); }
{multi_comment}  {} 
.                  { System.out.println("Illegal char, '" + yytext() + "' line: " + yyline + ", column: " + yychar); }