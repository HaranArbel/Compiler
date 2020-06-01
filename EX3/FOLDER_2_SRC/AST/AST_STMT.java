package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public abstract class AST_STMT extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}
	
    public abstract TYPE SemantMe() throws SemantMeException; 

}
