package AST;


public class AST_EXP_MINUS_INT extends AST_EXP
{
	public int value;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_MINUS_INT(int value)
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
}
