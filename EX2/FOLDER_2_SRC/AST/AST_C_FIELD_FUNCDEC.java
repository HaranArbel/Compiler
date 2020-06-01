package AST;

public class AST_C_FIELD_FUNCDEC extends AST_C_FIELD
{
	public AST_F_DEC fDec;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD_FUNCDEC(AST_F_DEC fDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (fDec != null) System.out.format("====================== cField -> fDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.fDec = fDec; 
	}

	/************************************************/
	/* The printing message for an FUNCDEC C FIELD AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CFIELD FUNCDEC */
		/*******************************/
		System.out.format("AST NODE C_FIELD_FUNCDEC\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE fDec EXPRESSIONS   */
        /*********************************************/
		if (fDec != null) fDec.PrintMe();
		
		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "cField\nfuncDec");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (fDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fDec.SerialNumber);
	}
}
