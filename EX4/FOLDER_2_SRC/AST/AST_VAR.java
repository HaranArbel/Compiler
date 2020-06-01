package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import CONTEXT.*;

public abstract class AST_VAR extends AST_Node
{
	public Context context;
	
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST_VAR NODE");
	}
	public abstract TYPE SemantMe() throws SemantMeException;

}

