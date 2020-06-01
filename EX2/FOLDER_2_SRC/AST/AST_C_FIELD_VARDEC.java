package AST;

import AST.AST_DEC;
import AST.AST_GRAPHVIZ;
import AST.AST_Node_Serial_Number;
import AST.AST_VAR_DEC;


public class AST_C_FIELD_VARDEC extends AST_C_FIELD
{
	public AST_VAR_DEC vDec;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD_VARDEC(AST_VAR_DEC vDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (vDec != null) System.out.format("====================== cField -> vDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.vDec = vDec; 
	}

	/************************************************/
	/* The printing message for an VARDEC C FIELD AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CFIELD VARDEC */
		/*******************************/
		System.out.format("AST NODE C_FIELD_VARDEC\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE fDec EXPRESSIONS   */
        /*********************************************/
		if (vDec != null) vDec.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "cField\nvDec");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (vDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vDec.SerialNumber);
	}
}
