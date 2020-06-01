package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public class AST_EXP_MINUS_INT extends AST_EXP
{
	public int value;
	public int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_MINUS_INT(int value, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> MINUS INT( %d )\n", value);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
		this.lineNumber=lineNumber;
	}

	/************************************************/
	/* The printing message for an MINUS INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE MINUS INT( %d )\n",value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("MINUS INT(%d)",value));
	}

	public TYPE SemantMe() throws SemantMeException{
        return TYPE_INT.getInstance();
	}
	
	public boolean isConst() {
		return true;
	}
}
