package AST;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.TEMP;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public TYPE SemantMe() throws SemantMeException {
		return null;
	}
	/*****************************************/
	/* The default IR action for an AST node */
    /*****************************************/
    public TEMP IRme() {
        return null;
    }

	
}
