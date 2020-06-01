package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;


public class AST_EXP_INT extends AST_EXP
{
	public int value;
	private int lineNumber;
	int MAX_INT = 32768;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_INT(int value, int lineNumber)
	{

		System.out.println("in AST_EXP_INT\n");		

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> INT( %d )\n", value);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.value = value;
		this.lineNumber=lineNumber;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE INT( %d )\n",value);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("INT(%d)",value));
	}
	
	public TYPE SemantMe() throws SemantMeException{

		if(this.value == MAX_INT) throw new SemantMeException(lineNumber, "MAX_INT_BOUND");
        return TYPE_INT.getInstance();
	}
	
	public boolean isConst() {
		return true;
	}

	public TEMP IRme() {
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommandConstInt(t, this.value));
        return t;
    }
}
