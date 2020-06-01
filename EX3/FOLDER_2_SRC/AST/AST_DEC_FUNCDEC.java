package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_DEC_FUNCDEC extends AST_DEC
{
	
	public AST_F_DEC fDec;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_FUNCDEC(AST_F_DEC dec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> funcDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		fDec=dec;
	}
	
	/***********************************************/
	/* The default message for an dec varDec AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = DEC FUNCDEC AST NODE */
		/************************************/
		System.out.print("AST NODE DEC FUNCDEC\n");

		/*****************************/
		/* RECURSIVELY PRINT funcDec ... */
		/*****************************/
		if (fDec != null) fDec.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nto\nFUNCDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (fDec != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fDec.SerialNumber);
			
	}
	
	public TYPE SemantMe() throws SemantMeException {
		return fDec.SemantMe();
	}
}
