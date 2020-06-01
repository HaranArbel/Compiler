package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_EXP_STRING extends AST_EXP
{
	public String str;
	private int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(String str, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> STERING( %s )\n", str);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.str = str;
		this.lineNumber=lineNumber;
	}

	/************************************************/
	/* The printing message for an STRING EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST STRING EXP */
		/*******************************/
		System.out.format("AST NODE STRING( %s )\n",str);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STRING(%s)",str));
	}
	
	public TYPE SemantMe() throws SemantMeException{
        return TYPE_STRING.getInstance();
	}
	
	public boolean isConst() {
		return true;
	}
}
