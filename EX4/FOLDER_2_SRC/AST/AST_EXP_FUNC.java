package AST;

import IR.*;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;

import static AST.AST_EXP_BINOP.*;


public class AST_EXP_FUNC extends AST_EXP
{
	public AST_VAR var;
	public String id;
	public AST_EXP_PARAMS params;
	private SYMBOL_TABLE symbTable;
	public int lineNumber;
	//added
	public String startLabel;
	public int numParams;
	public int vTableOffset;
	boolean isMethod;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_FUNC(AST_VAR var,  String id, AST_EXP_PARAMS params, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		if (var != null && params != null) System.out.format("====================== exp -> var.%s(exp list)\n", id);
		else if (var != null && params == null) System.out.format("====================== exp -> var.%s()\n", id);
		else if (var == null && params != null) System.out.format("====================== exp -> %s(exp list)\n", id);
		else System.out.format("====================== exp -> %s()\n", id);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.params = params;
		this.symbTable = SYMBOL_TABLE.getInstance();
		this.lineNumber = lineNumber;
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE AST_EXP_FUNC\n");
		if (var!=null) var.PrintMe();
		if (params!=null) params.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST_EXP_FUNC: %s\n",id));
		
		if(var!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if(params!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);		
	}

	/************************************************/
	/* Semanting the AST node AST_EXP_FUNC */
	/************************************************/
    public TYPE SemantMe() throws SemantMeException
    {
    	TYPE_FUNCTION function = null;

    	if( var != null )//like calling a.foo()
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
	        TYPE foundType = symbTable.find(id);
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
        /*     Return the  functions return_Type because it is expression. */
	    if (params == null && function.params == null)  return function.returnType;
	    if (params != null && function.params != null)
	    {
	        TYPE_LIST passed_param_list = params.SemantMe();
	        for(TYPE_LIST expected_param_list = function.params; expected_param_list != null; expected_param_list = expected_param_list.tail)
	        { 
	            if( passed_param_list == null || !Helper.isAssignmentValid(expected_param_list.head, passed_param_list.head, false))
	            {
	                throw new SemantMeException(lineNumber, "Function params doesn't match called params");
	            }
	            passed_param_list = passed_param_list.tail;
	        }
	        /*Return the  functions return_Type because it is expression. */
	        return function.returnType;
	    }
	    else  throw new SemantMeException(lineNumber, "Function params doesn't match called params");
    }



	/************************************************/
	/* IRme the AST node AST_EXP_FUNC */
	/************************************************/
	@Override
	public TEMP IRme() {

		/* get $t0-$t7), $ra, params, curr $fp*/
		int prologSize = -4 * (10+this.numParams);

		AST_EXP_PARAMS paramsLst = this.params;

		// allocate place on stack
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $sp,$sp,%d", prologSize), null, false));

		// now, $sp is next-fp

		//put current fp on (next-fp)+0
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("sw $fp,0($sp)"), null, false));

		if (!this.id.contains("class_init")) {
			TEMP funcName = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IR_general_command(String.format("la $t%%d,%s", "func_"+this.id), funcName, true));
			IR.getInstance().Add_IRcommand(new IRcommand_Store(funcName, -4));
		}

		int count = 0;
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
			count++;
		}

		TEMP argument;
		// needed to take care the first param because of the next are from diferent type 
		if(count < this.numParams){
			argument = paramsLst.head.IRme();
			IR.getInstance().Add_IRcommand(new IR_general_command( String.format("sw $t%%d,%d($sp)", 4 * (count + 1)), argument, false));
			count++;

			AST_EXP_PARAMS_LIST params = paramsLst.paramsList;
			// evaluate params and store them on right place
			for(; count < this.numParams; count++) {
				argument = params.head.IRme();
				IR.getInstance().Add_IRcommand(new IR_general_command( String.format("sw $t%%d,%d($sp)", 4 * (count + 1)), argument, false));
				params= params.tail;
			}
		}

		// backup frame info
		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(this.numParams, false));

		//set new fp to end of stack
		IR.getInstance().Add_IRcommand(new IR_general_command(String.format("addi $fp,$sp,0"), null, false));

		// call the function
		if(!this.isMethod) {
			IR.getInstance().Add_IRcommand(new IR_general_jump_command(this.startLabel));
		}
		else {
			IRmethod();
		}

		// restore frame
		IR.getInstance().Add_IRcommand(new IR_prologue_epilogue_command(numParams, true));

		// $v0 - return value
		TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IR_move_command(null, null, res, "$v0"));
		return res;
	}

	public void IRmethod(){
		TEMP temp_0 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP temp_1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP temp_2 = TEMP_FACTORY.getInstance().getFreshTEMP();

		// load instance  address
		IR.getInstance().Add_IRcommand(new IR_general_command("lw $t%d, 4($fp)", temp_0, true));

		//get struct address
		IR.getInstance().Add_IRcommand(new IRcommand_Load(temp_1, temp_0, 0, null, false));

		// set t2 to address of function
		IR.getInstance().Add_IRcommand(new IRcommand_Load(temp_2, temp_1, this.vTableOffset * 4, null, false));

		//jal to there
		IR.getInstance().Add_IRcommand(new IR_general_jump_command(temp_2));
	}

}

