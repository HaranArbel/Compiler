package AST;


public class AST_STMT_VARDEC extends AST_STMT
{
	
	/***************/
	/*  varDec */
	/***************/
	public AST_VAR_DEC varDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_VARDEC(AST_VAR_DEC varDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.varDec = varDec;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE STMT VARDEC\n");

		/***********************************/
		/* RECURSIVELY PRINT EXP ... */
		/***********************************/
		if (varDec != null) varDec.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (varDec != null) AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT\nvardec");
		else AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "RETURN;");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (varDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
		
	}
}
