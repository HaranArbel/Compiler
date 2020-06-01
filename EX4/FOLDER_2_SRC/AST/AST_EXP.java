package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;


public abstract class AST_EXP extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST expression node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST EXP NODE");
	}
	 
    public boolean isConst() {
        return false;
    }

    public abstract TEMP IRme();
}

