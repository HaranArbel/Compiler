package AST;

import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;
import IR.*;


public class AST_STMT_FUNC extends AST_STMT
{

	public AST_VAR var;
	public String id;
	public AST_EXP_PARAMS params;
	private SYMBOL_TABLE symbolTable;
	private int lineNumber;
	//added
	public String startLabel;
	public int numParams;
	public int vTableOffset;
	boolean isMethod;
	
	
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

		// added - mips handle
		this.startLabel = function.startLabel;
		this.numParams = function.numParams;
		if(function.belongsToClass != null) { //this is a function class
			this.numParams++; // for "this" :)
			this.isMethod = true;
			this.vTableOffset = function.vTableOffset;
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


	/************************************************/
	/* IRme the AST node AST_EXP_FUNC */
	/************************************************/
	@Override
	public TEMP IRme() {

	    if("PrintTrace".equals(id) && !this.isMethod) {
			IR.getInstance().Add_IRcommand(new IRCommandPrintTrace());
			return null;
		}

		AST_EXP_PARAMS paramsLst = this.params;

		int prologSize = - 4 * ( 10+this.numParams );
		// allocate place on stack
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $sp,$sp,%d", prologSize), null, false));

		//put current fp on (next-fp)+0
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $fp,0($sp)"), null, false));

		if (!this.id.contains("class_init")) {
			TEMP funcName = TEMP_FACTORY.getInstance().getFreshTEMP();
			if( ("PrintInt".equals(id) || "PrintString".equals(id)) &&  !this.isMethod )
				IR.getInstance().Add_IRcommand(new IR_general_command(String.format("la $t%%d,%s", this.id), funcName, true));
			else
				IR.getInstance().Add_IRcommand(new IR_general_command(String.format("la $t%%d,%s", "func_"+this.id), funcName, true));
			IR.getInstance().Add_IRcommand(new IRcommand_Store(funcName, -4));
		}

		int i = 0;
		if(this.isMethod) {
			TEMP ths;
			if(this.var != null) {
				AST_EXP_VAR ev = new AST_EXP_VAR(this.var, lineNumber);
				ths = ev.IRme();
				IR.getInstance().Add_IRcommand(new IRcommand_Check_For_Null_Pointer_Dereference(ths));
			} else {
				ths = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", ths, true));
			}
			IR.getInstance().Add_IRcommand(new IR_general_command("sw $t%d,4($sp)", ths, false));
			i++;
		}

		if(i < this.numParams){
			TEMP argValue = paramsLst.head.IRme();
			IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $t%%d,%d($sp)", 4 * (i + 1)), argValue, false));
			i++;
		
			AST_EXP_PARAMS_LIST params = paramsLst.paramsList;
			// evaluate params and store them on right place
			for(; i < this.numParams; i++) {
				argValue = params.head.IRme();
				IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $t%%d,%d($sp)", 4 * (i + 1)), argValue, false));
				params= params.tail;
			}
		}

		// store frame info
		IR.getInstance().Add_IRcommand(new IRcommandFrameBackup(this.numParams));
		// backup frame info
		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(this.numParams, false));

		//set new fp to end of stack
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $fp,$sp,0"), null, false));

		// if the function is of a class
		if(this.isMethod) {
			irStmtMethod();
		}
		else {
			if("PrintInt".equals(id) || "PrintString".equals(id) )
				IR.getInstance().Add_IRcommand(new IR_general_jump_command(id));
			else
				IR.getInstance().Add_IRcommand(new IR_general_jump_command(this.startLabel));
		}

		// restore frame
		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(numParams, true));

		// return from function call - result in $v0
		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IR_move_command(null, null, res, "$v0"));
		return res;

	}


	public void irStmtMethod(){
		TEMP t0 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t2 = TEMP_FACTORY.getInstance().getFreshTEMP();

		//get "this" - the struct address
		IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", t0, true));

		//get struct address
		IR.getInstance().Add_IRcommand(new IRcommand_Load(t1, t0, 0, null, false));

		//get the address of this's function into t2
		IR.getInstance().Add_IRcommand(new IRcommand_Load(t2, t1, this.vTableOffset * 4, null, false));

		//jal to there
		IR.getInstance().Add_IRcommand(new IR_general_jump_command(t2));
	}
}
