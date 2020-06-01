package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_C_FIELD_FUNCDEC extends AST_C_FIELD
{
	public AST_F_DEC fDec;
	private SYMBOL_TABLE symbolTable;
	private int lineNumber;

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD_FUNCDEC(AST_F_DEC fDec, int lineNumber)
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
		this.symbolTable = SYMBOL_TABLE.getInstance();
		this.lineNumber = lineNumber;
	}
    
    public boolean isDataMember() {
        return false;
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

	public TYPE SemantSignature() throws SemantMeException {

		fDec.SemantSignature();
		//return value (TYPE) is stored  in the class's methods TYPE_LIST
		return symbolTable.find(fDec.name);
	}
	public TYPE SemantBody() throws SemantMeException {
		fDec.SemantBody();
		//return value is irrelevant - function's type was stored in SemantSignature
		return null;
	}

}
