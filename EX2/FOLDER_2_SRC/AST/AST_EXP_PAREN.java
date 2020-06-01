package AST;

public class AST_EXP_PAREN extends AST_EXP{
	public AST_EXP exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_PAREN(AST_EXP e)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> (exp)\n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
	}

	/************************************************/
	/* The printing message for an NIL EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST NIL EXP */
		/*******************************/
		System.out.format("AST NODE AST_EXP_PAREN\n");


		if (exp!=null) exp.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST_EXP_PAREN"));

		if(exp!=null)
			AST_GRAPHVIZ.getInstance().logEdge(
				SerialNumber,
				exp.SerialNumber);
	}
	
}



