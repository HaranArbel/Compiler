package AST;
//import ERRORS.SemantMeException;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public abstract class AST_DEC extends AST_Node
{
    /*********************************************************/
	/* The default message for an unknown AST DEC node */
    /*********************************************************/
    public void PrintMe()
    {
            System.out.print("UNKNOWN AST DECLARATION NODE");
    }
    public abstract TYPE SemantMe() throws SemantMeException; 

}
