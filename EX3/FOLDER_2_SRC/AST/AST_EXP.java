package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public abstract class AST_EXP extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST expression node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST EXP NODE");
	}
	
    public abstract TYPE SemantMe() throws SemantMeException; 
    
    public boolean isConst() {
        return false;
    }
}

