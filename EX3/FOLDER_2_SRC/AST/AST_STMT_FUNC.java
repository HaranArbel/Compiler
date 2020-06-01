package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_STMT_FUNC extends AST_STMT
{

	public AST_VAR var;
	public String id;
	public AST_EXP_PARAMS params;
	private SYMBOL_TABLE symbolTable;
	private int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_FUNC(AST_VAR var,  String id, AST_EXP_PARAMS params, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		if (var != null && params != null) System.out.format("====================== stmt -> var.%s(exp list)\n", id);
		else if (var == null && params != null) System.out.format("====================== stmt -> %s(exp list)\n", id);
		else if (var != null && params == null) System.out.format("====================== stmt -> var.%s()\n", id);
		else System.out.format("====================== stmt -> %s()\n", id);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.params = params;
		this.symbolTable = SYMBOL_TABLE.getInstance();
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
		System.out.format("AST NODE AST_STMT_FUNC\n");
		
		
		if(var!=null) var.PrintMe();		
		if(params!=null) params.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST_STMT_FUNC\n%s",id));
		if(var!=null)	AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if(params!=null)	AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
	}

	public TYPE SemantMe() throws SemantMeException
    {
		TYPE_FUNCTION function = null;       
    	if( var != null )
    	{
    		TYPE varType = var.SemantMe();
			/* [0] semant var TYPE and ensure it is a CLASS_TYPE. */
	        if ( !varType.isClass() )  throw new SemantMeException(lineNumber, varType.name + " is expected to be of type class");;
			/* [1] Find if id is a method in var class or in it parents*/
	        function = ((TYPE_CLASS)varType).findMethod(id);
	        if (function == null) throw new SemantMeException(lineNumber, "A method named " + id + " wasn't found in class: " + varType.name);
	    }
	    else  // var == null
	    { 
	        TYPE foundType = symbolTable.find(id);
	        /* [0] Find if id is a function*/
	        if ( foundType == null )        throw new SemantMeException(lineNumber, "Function wasn't declared(" + id + ")");
	        if ( !foundType.isFunction() )  throw new SemantMeException(lineNumber, id + " expected to be of function type");
	        function = (TYPE_FUNCTION) foundType;
	    }
        /* [2] Match the passed parameters and the expected params of the found function,*/
        /*     Return null because it is statment. */
	    if (params == null && function.params == null)  return null;
	    if (params != null && function.params != null) 
	    {
	        TYPE_LIST passed_param_list = params.SemantMe();
	        for(TYPE_LIST expected_param_list = function.params; expected_param_list != null; expected_param_list = expected_param_list.tail)
	        { 
	            if( passed_param_list == null || !Helper.isAssignmentValid( expected_param_list.head, passed_param_list.head, false) )
	            {
	                throw new SemantMeException(lineNumber, "Function params doesn't match called params");
	            }
	            passed_param_list = passed_param_list.tail;
	        }
	        /* Return null because it is statment. */
	        return null;
	    }
	    else  throw new SemantMeException(lineNumber, "Function params doesn't match called params");
    }

}
