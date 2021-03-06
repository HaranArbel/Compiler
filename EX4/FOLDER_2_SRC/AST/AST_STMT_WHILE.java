package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;
	private SYMBOL_TABLE symbolTable;
	private int lineNumber;
	
	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> WHILE (exp) {stmt [stmt]*}\n");
	
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/

		this.cond = cond;
		this.body = body;
		this.lineNumber=lineNumber;
		this.symbolTable=SYMBOL_TABLE.getInstance();
	}
	
	
	public void PrintMe()
	{
		
		/*************************************/
		/* AST NODE TYPE = AST STMT IF */
		/*************************************/
		System.out.print("AST STMT WHILE EXP\n");

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
			String.format("AST STMT WHILE"));
		
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
        	throw new SemantMeException(lineNumber, "condition inside WHILE is not INT");
        }
        
        /*****************************/
        /* [3] Semant The Statements */
        /*****************************/
        body.SemantMe();
        
        /*****************/
        /* [3] End Scope */
        /*****************/
        this.symbolTable.endScope();
		
        /*********************************************************/
        /* [4] Return value is irrelevant for While */
        /*********************************************************/
        return null;
    }

	public TEMP IRme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("WHILE_END");
		String label_start = IRcommand.getFreshLabel("WHILE_START");
	
		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_start));

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRme();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));		

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.IRme();

		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_Label(label_start));		

		/**********************/
		/* [7] Loop end label */
		/**********************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_end));

		/*******************/
		/* [8] return null */
		/*******************/
		return null;
	}

    
}

