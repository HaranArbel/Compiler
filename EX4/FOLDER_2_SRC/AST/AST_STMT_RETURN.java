package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_STMT_RETURN extends AST_STMT
{
	/***************/
	/*  return; or return exp; */
	/***************/
	public AST_EXP exp;
	public int lineNumber;
	public String endLabel;
	private SYMBOL_TABLE symbTable;
	///NEW!!!
	String funcEndLabel;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp, int lineNumber)
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
		this.lineNumber = lineNumber;
		this.symbTable = SYMBOL_TABLE.getInstance();
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


 	@Override
    public TYPE SemantMe() throws SemantMeException
    {
		this.funcEndLabel = symbTable.getCurrentEndLabel();
    	TYPE return_type = null; 
    	TYPE expected_ReturnValue = symbTable.getCurrnetReturnType();
        if (expected_ReturnValue == null )  
			throw new SemantMeException(lineNumber, "return statements are allowed only inside functions\n");
		
		//return exp;
        if ( exp != null )
        {
			return_type = exp.SemantMe();
			if( Helper.currentReturnIsValid(expected_ReturnValue, return_type))
				return return_type;
        } 

		//return;
        else  
        {  
        	if( expected_ReturnValue == TYPE_VOID.getInstance()	) 
				return TYPE_VOID.getInstance();
	    }

		//If we reached this point throw an exception
 		throw new SemantMeException(lineNumber, "returned value is invalid");
    }

    public TEMP IRme() {
        if (exp != null) {
            TEMP et = exp.IRme();
            IR.getInstance().Add_IRcommand(new IR_move_command("$v0", et, null, null));
        }
    	IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(funcEndLabel));
		return null;
    }
}
