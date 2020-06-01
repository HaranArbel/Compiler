public interface TokenNames {
  
  public static final int EOF = 0;
  public static final int ERROR = 1;
  
  public static final int INT = 2;
  public static final int ID = 3; 
  public static final int STRING = 4; 
  
 
  /* operators */
  public static final int PLUS = 6;
  public static final int MINUS = 7;
  public static final int TIMES = 8;
  public static final int DIVIDE = 9;
  public static final int EQ = 10;
  public static final int LT = 11;
  public static final int GT = 12;
    public static final int ASSIGN = 13;
  
  /* terminals */ 
  public static final int LPAREN = 14;
  public static final int RPAREN = 15;
  public static final int LBRACE = 16;
  public static final int RBRACE = 17;
  public static final int LBRACK = 18;
  public static final int RBRACK = 19;
  public static final int SEMICOLON = 20;
  public static final int COMMA = 21;
  public static final int DOT = 22;
  
  /* saved words */
  public static final int NIL = 23;
  public static final int ARRAY = 24;
  public static final int WHILE = 25;
  public static final int EXTENDS = 26;
  public static final int RETURN = 27;
  public static final int NEW = 28;
  public static final int IF = 29;
  public static final int CLASS = 30;
}







