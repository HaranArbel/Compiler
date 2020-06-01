package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public abstract class AST_NEW_EXP extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST new expression node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST NEW EXP NODE");
	}
	
    public abstract TYPE SemantMe() throws SemantMeException; 
    
}

