package AST;

import AST.AST_DEC;
import AST.AST_GRAPHVIZ;
import AST.AST_Node_Serial_Number;
import AST.AST_VAR_DEC;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public class AST_C_FIELD_VARDEC extends AST_C_FIELD
{
	public AST_V_DEC vDec;
 	private SYMBOL_TABLE symbolTable;
 	public int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD_VARDEC(AST_V_DEC vDec, int lineNumber)
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
 		this.symbolTable = SYMBOL_TABLE.getInstance();
 		this.lineNumber = lineNumber;
	}

    public boolean isDataMember() {
        return true;
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


	public TYPE SemantSignature() throws SemantMeException {
		return null;
	}

	public TYPE_CLASS_VAR_DEC SemantBody() throws SemantMeException {

		assert (vDec != null);

 		// assert that vDec expression is a constant if exists
		// notice that newExp is not a const by definition of the deriviation rules.
		if (vDec.newExp != null) {
			throw new SemantMeException (lineNumber, "A declared data member inside a class can be initialized only with a constant value");
		}

		if (vDec.exp != null) {
			if (!vDec.exp.isConst())
	 			throw new SemantMeException (lineNumber, "A declared data member inside a class can be initialized only with a constant value");
		}

 		vDec.SemantMe();
		
		//return value (TYPE) is stored  in the class's datamembers TYPE_LIST
 		return new TYPE_CLASS_VAR_DEC(symbolTable.find(vDec.name), vDec.name, vDec.context); 
 	}

	// no need to do IRme
}
