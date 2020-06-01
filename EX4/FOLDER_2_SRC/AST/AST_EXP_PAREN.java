package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;

public class AST_EXP_PAREN extends AST_EXP{
	public AST_EXP exp;
	private int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_PAREN(AST_EXP exp, int lineNumber)
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
		this.lineNumber=lineNumber;
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
	
	public TYPE SemantMe() throws SemantMeException{
		return exp.SemantMe();
	}

    public TEMP IRme(){
        return exp.IRme();
    }	
}



