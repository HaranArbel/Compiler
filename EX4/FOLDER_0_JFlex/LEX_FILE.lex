/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	 private final Integer MAX_INT =   32768;
	 private final Integer MIN_INT =  -32768;
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 
	
	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public String printToken(int id) { 
		switch(id){
			case 0: return "EOF";
			
			case 2:	return "INT";
			case 3:	return "ID";
			case 4:	return "STRING";

			case 6:	return "PLUS";
			case 7:	return "MINUS";
			case 8:	return "TIMES";
			case 9:	return "DIVIDE";
			case 10: return "EQ";
			case 11: return "LT";
			case 12: return "GT";
			case 13: return "ASSIGN";
			
			case 14: return "LPAREN";
			case 15: return "RPAREN";
			case 16: return "LBRACE";
			case 17: return "RBRACE";
	
			case 18: return "LBRACK";
			case 19: return "RBRACK";
			case 20: return "SEMICOLON";
			case 21: return "COMMA";
			case 22: return "DOT";
			
			case 23: return "NIL";
			case 24: return "ARRAY";
			case 25: return "WHILE";
			case 26: return "EXTENDS";
			case 27: return "RETURN";
			case 28: return "NEW";
			case 29: return "IF";
			case 30: return "CLASS";
			default:	return "";
		}
		} 
	
	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getCharPos() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n

DIGIT          = [0-9]
LETTER         = [a-zA-Z]

WhiteSpace		= {LineTerminator} | [ \t\f]
INTEGER			= [1-9]{DIGIT}* | 0 
ID				= {LETTER}({LETTER}|{DIGIT})*
STRING  		= \"{LETTER}*\"
ERROR_NUM       = 0{DIGIT}+ 

CommentChar	=  [a-zA-Z0-9 \t\f\(\)\[\]\{\}\?\!\+\-\.;]
InvalidLineCommentChar  = [^a-zA-Z0-9 \t\f\r\n\r\n\(\)\[\]\{\}\?\!\+\-\*\/\.;]
LineCommentChar = {CommentChar} | [\*\/]
MultiLineCommentChar = \**{CommentChar} | [\/] | {LineTerminator}

LineComment	= \/\/{LineCommentChar}*
MultiLineComment = \/\*{MultiLineCommentChar}*\*+\/
InvalidLineComment      = \/\/.*{InvalidLineCommentChar}.*
InvalidMultiLineComment = \/\*{MultiLineCommentChar}*


/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

"+"					{ return symbol( TokenNames.PLUS);}
"-"					{ return symbol( TokenNames.MINUS);}
"*"					{ return symbol( TokenNames.TIMES);}
"/"					{ return symbol( TokenNames.DIVIDE);}
"("					{ return symbol( TokenNames.LPAREN);}
")"					{ return symbol( TokenNames.RPAREN);}
"{"					{ return symbol( TokenNames.LBRACE);}
"}"					{ return symbol( TokenNames.RBRACE);}
"["					{ return symbol( TokenNames.LBRACK);}
"]"					{ return symbol( TokenNames.RBRACK);}
","					{ return symbol( TokenNames.COMMA);}
"."					{ return symbol( TokenNames.DOT);}
";"					{ return symbol( TokenNames.SEMICOLON);}
":="				{ return symbol( TokenNames.ASSIGN);}
"="					{ return symbol( TokenNames.EQ);}
"<"					{ return symbol( TokenNames.LT);}
">"					{ return symbol( TokenNames.GT);}
"class"             { return symbol( TokenNames.CLASS); }
"nil"               { return symbol( TokenNames.NIL); }
"array"             { return symbol( TokenNames.ARRAY); }
"while"             { return symbol( TokenNames.WHILE); }
"extends"           { return symbol( TokenNames.EXTENDS); }
"return"            { return symbol( TokenNames.RETURN); }
"new"               { return symbol( TokenNames.NEW); }
"if"                { return symbol( TokenNames.IF); }

{LineComment}	    { /* just skip what was found, do nothing */ }
{MultiLineComment}  { /* just skip what was found, do nothing */ }
{InvalidLineComment}	  { return symbol( TokenNames.error); }
{InvalidMultiLineComment} { return symbol( TokenNames.error); }
{INTEGER}			{ 
						Integer val = new Integer(yytext());
						if ( val < MIN_INT || MAX_INT < val ) return symbol( TokenNames.error);
						else return symbol( TokenNames.INT, val );
					}  
{ID}				{ return symbol( TokenNames.ID,  new String( yytext()));} 
{STRING}  			{ return symbol( TokenNames.STRING,  new String( yytext()));} 
{ERROR_NUM}  		{ return symbol( TokenNames.error);} 
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
<<EOF>>				{ return symbol( TokenNames.EOF);}


}
