package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;


public class AST_EXP_NIL extends AST_EXP
{
	public int lineNumber;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_NIL(int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> NIL\n");

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
		System.out.format("AST NODE NIL\n");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("NIL"));
	}

	public TYPE SemantMe() throws SemantMeException{
        return TYPE_NIL.getInstance();
	}
	
	public boolean isConst() {
		return true;
	}
	public TEMP IRme() {
		TEMP t0 = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(t0, 0));
		return t0;
	}
}
