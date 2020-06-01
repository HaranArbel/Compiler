package AST;

public class AST_STMT_RETURN extends AST_STMT
{
	/***************/
	/*  return; or return exp; */
	/***************/
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp != null) System.out.print("====================== stmt -> RETURN exp SEMICOLON\n");
		else System.out.print("====================== stmt -> RETURN SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE RETURN STMT\n"); 

		/***********************************/
		/* RECURSIVELY PRINT EXP ... */
		/***********************************/
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "RETURN exp;");
		else AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "RETURN;");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
		
	}
}
