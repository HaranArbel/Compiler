package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;
	private SYMBOL_TABLE symbolTable;
	public int lineNumber;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body, int lineNumber)
	{
			
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> IF (exp) {stmtList}\n");
	
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.symbolTable= SYMBOL_TABLE.getInstance();
		this.lineNumber=lineNumber;
	}
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		
		/*************************************/
		/* AST NODE TYPE = AST STMT IF */
		/*************************************/
		System.out.print("AST STMT IF\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST STMT IF"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
    
	
	  
    public TYPE SemantMe() throws SemantMeException
    {

        /*************************/
        /* [1] Begin While Scope */
        /*************************/
    	this.symbolTable.beginScope();
       

        /****************************/
        /* [2] Semant the Condition */
        /****************************/
        if (cond.SemantMe() != TYPE_INT.getInstance())
        {
        	throw new SemantMeException(lineNumber, "IF condition was not evaluated to INT");
        }
        
        /*****************************/
        /* [3] Semant The Statements */
        /*****************************/
        body.SemantMe();
        
        /*****************/
        /* [4] End Scope */
        /*****************/
        this.symbolTable.endScope();
        
		/*********************************************************/
        /* [5] Return value is irrelevant for While */
        /*********************************************************/
        return null;
    }    
}

