package AST;

public class AST_PROG extends AST_Node{

public AST_DEC_LIST decList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PROG( AST_DEC_LIST decs)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (decs != null) System.out.format("====================== program -> decList\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.decList = decs; 
	}

	/************************************************/
	/* The printing message for an VARDEC C FIELD AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CFIELD VARDEC */
		/*******************************/
		System.out.format("AST NODE PROG\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE fDec EXPRESSIONS   */
        /*********************************************/
		if (decList != null) decList.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "PROGRAM");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (decList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,decList.SerialNumber); 
	}
}

	
	

