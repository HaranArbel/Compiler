package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public abstract class AST_C_FIELD extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST cField node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST CFIELD NODE");
	}
	
	public abstract TYPE SemantSignature() throws SemantMeException;

	public abstract TYPE SemantBody() throws SemantMeException;

	public abstract boolean isDataMember();

}
