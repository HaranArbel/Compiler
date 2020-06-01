package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public abstract class AST_VAR_DEC extends AST_DEC
{
	/*********************************************************/
	/* The default message for an unknown AST var declaration node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST VAR DECLARATION NODE");
	}
	
	public abstract TYPE SemantMe() throws SemantMeException;
}


