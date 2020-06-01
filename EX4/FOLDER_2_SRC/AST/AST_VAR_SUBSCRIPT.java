package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import IR.*;
import TEMP.*;


public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	public int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
		this.lineNumber=lineNumber;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}
	
	public TYPE SemantMe() throws SemantMeException {        
        TYPE varType = var.SemantMe();
        TYPE subscriptType = subscript.SemantMe();

        if (varType.isArray() && subscriptType == TYPE_INT.getInstance() )
        	return ((TYPE_ARRAY) varType).type;
        else
        	throw new SemantMeException(lineNumber, "Expected array type VAR and int type EXP when using subscript[]");
	}

	@Override
	public TEMP IRme() 
	{
		// [0] get fresh temps for array address, size,
	    TEMP arrayAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP arraySize = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP zeroReg = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();

		// [1] get fresh labels for 
		String not_too_small = IRcommand.getFreshLabel("Not_Too_Small");
		String not_too_big = IRcommand.getFreshLabel("Not_Too_Big");

		// [2] get fresh temps for arr and size
		TEMP subscriptIR = subscript.IRme();

		// [3] get fresh temps for arr and size
		TEMP varIR = var.IRme() ;

		// [4] get fresh temps for arr and size
		IR.getInstance().Add_IRcommand(new IRcommand_Load(arrayAddress, varIR));
		IR.getInstance().Add_IRcommand(new IRcommand_Check_For_Null_Pointer_Dereference(arrayAddress));

		// [5] check access violation (make sure that subscrupt value < array size)
		IR.getInstance().Add_IRcommand(new IRcommand_Load(arraySize, arrayAddress));
		IR.getInstance().Add_IRcommand(new IR_branch_less(subscriptIR, arraySize, not_too_big, false));
		IR.getInstance().Add_IRcommand(new IRcommand_Runtime_error("string_access_violation"));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(not_too_big));

		// [6] check access violation (make sure that subscrupt value >= 0)
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(zeroReg, 0));
		IR.getInstance().Add_IRcommand(new IR_branch_less(zeroReg, subscriptIR, not_too_small, true));
		IR.getInstance().Add_IRcommand(new IRcommand_Runtime_error("string_access_violation"));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(not_too_small));

		// [7]commit array  access
		IR.getInstance().Add_IRcommand(new IRcommand_array_access(res, arrayAddress, subscriptIR));

		// [8] return address of var[exp] 
		return res;
	}
}
