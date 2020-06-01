package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import MIPS.*;
import CONTROL_FLOW.*;
import TEMP.*;

import java.util.ArrayList;
import java.util.List;


public class AST_PROG extends AST_Node{

	public AST_DEC_LIST decList;
	public int lineNumber;
	public ArrayList<AST_V_DEC> globals = new ArrayList<>();

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PROG( AST_DEC_LIST decs, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (decs != null) System.out.format("====================== program -> decList\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.decList = decs; 
		this.lineNumber=lineNumber;
	}

	/************************************************/
	/* The printing message for an VARDEC C FIELD AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CFIELD VARDEC */
		/*******************************/
		System.out.format("AST NODE PROG\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE fDec EXPRESSIONS   */
        /*********************************************/
		if (decList != null) decList.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "PROGRAM");

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (decList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,decList.SerialNumber); 
	}
	
	public TYPE SemantMe() throws SemantMeException {
	    return decList.SemantMe();
	}

	public TEMP IRme() {
		
		AST_F_DEC.IRPrintInt();
		AST_F_DEC.IRPrintString();
		AST_DEC_LIST decPtr = decList;
		
		while (decPtr != null) {
    	    if (decPtr.head instanceof AST_V_DEC) 
    	    {
				this.globals.add((AST_V_DEC)decPtr.head);
			} 
			else 
			{
				decPtr.head.IRme();
			}
			decPtr = decPtr.tail;
		}

		IR.getInstance().Add_IRcommand(new IRcommand_Label("main"));
		
		for (AST_V_DEC globalVar : this.globals)
		{
			globalVar.IRme();
		}

		IR.getInstance().Add_IRcommand(new IR_general_jump_command("our_main"));
		return null;
	}
	
}

	
	

