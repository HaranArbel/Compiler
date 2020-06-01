package AST;

import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import CONTEXT.*;
import CONTROL_FLOW.*;
import TEMP.*;
import IR.*;
import java.util.ArrayList;
import java.util.List;

import static MIPS.sir_MIPS_a_lot.*;
import MIPS.*;



public class AST_F_DEC extends AST_DEC
{
	public String type;
	public String name;
	public AST_FUNC_DEC_PARAMS params;
	public AST_STMT_LIST stmtList;
	private SYMBOL_TABLE st;
	private int lineNumber;
	//added variables
	public String startLabel;
	public String endLabel;
	public int numberOfLocals;
	private boolean isMainFunction;
	public static int localVarsCounter = -1;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_F_DEC(String type, String name, AST_FUNC_DEC_PARAMS params, AST_STMT_LIST stmtList, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (params == null) System.out.format("====================== funcDec -> ID( %s ) ID( %s ) () {stmtList} ;\n", type, name);
		else System.out.format("====================== funcDec -> ID( %s ) ID( %s ) (params) {stmtList};\n", type, name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.name = name;
		this.params = params;
		this.stmtList = stmtList;
		this.lineNumber = lineNumber;
		this.st = SYMBOL_TABLE.getInstance();
	}

	/************************************************/
	/* The printing message for an AST FUNC DEC node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST FUNC DEC */
		/*******************************/
		System.out.format("AST NODE FUNC_DEC\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE params and stmtList EXPRESSIONS   */
        /*********************************************/
        if (params != null) params.PrintMe();
        if (stmtList != null) stmtList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/

	if(params!=null&&stmtList!=null)
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s(params) {stmtList}",type,name));
	}
	else if(params==null&&stmtList==null)
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s() {}",type,name));
	}
	else if(params==null)
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s() {stmtList}",type,name));
	}
	else
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s() {params}",type,name));
	}


		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
	if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
	if (stmtList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtList.SerialNumber);
	}



	/*******************Semant Signature**************/
	public TYPE SemantSignature() throws SemantMeException { // if f is a method

		/* [0] Check that the return value TYPE (funcType) exists */
		TYPE returnType = this.st.find(type);


		if (returnType == null) {
			throw new SemantMeException(lineNumber, "The return type was not de	fined");
		}
		/* [1] Check that the return value is valid */
		if (!Helper.isValidType(returnType, type) && returnType != TYPE_VOID.getInstance()){
			throw new SemantMeException(lineNumber, "The return type is not valid");
		}

		/* [2] Check a TYPE with same name is not declared in same scope. */
		if (!this.st.IsVarNameNew(name,false)) {
			throw new SemantMeException(lineNumber, name + " was already declared");
		}

		// begin scope for temporarily save params in symbol table. (will be removed when semant signature ends.
		this.st.beginScope();


		/* [4] Semant input params - we want the params */
		TYPE_LIST paramTypes = (params != null) ? params.SemantMe() : null;


		////NEW!!!
		////NEW!! get numParams
		int numParams = getNumParams(paramTypes);

		//////NEW!!!!!
		// check if func is main

		this.startLabel = IRcommand.getFreshLabel(this.name);

		this.endLabel = IRcommand.getFreshLabel(this.name + "_end");

		TYPE_FUNCTION function = new TYPE_FUNCTION(returnType, name, paramTypes, numParams, startLabel, endLabel);
		///////


		/* [5] Check if overriding is legal */
		if (this.st.getCurrParentClass() != null) {

			if (this.st.getCurrParentClass().findDataMember(name) != null)
				throw new SemantMeException(lineNumber, "AST_V_DEC: name is already a name of a data member in one of the class parents");

			TYPE_FUNCTION parentFunc = this.st.getCurrParentClass().findMethod(name);
			if (parentFunc != null) {
				if (!Helper.compareTypes(parentFunc.returnType, returnType))
				{
					throw new SemantMeException(lineNumber, "The Overriding func return type doesn't match the overridden func return type");
				}
				// Check that the parentFunc has the same param types
				if (paramTypes != null || parentFunc.params != null) {

					TYPE_LIST paramsList = paramTypes; //Notice that we change the pinter of the list if we iterate through it.
					TYPE_LIST parentParamsList = parentFunc.params;

					while (paramsList != null && parentParamsList != null) {
						if (!Helper.compareTypes(paramsList.head, parentParamsList.head)) {
							throw new SemantMeException(lineNumber, "The parameters of the overriden function does not match");
						}
						paramsList = paramsList.tail;
						parentParamsList = parentParamsList.tail;
					}
					// Check that there are no more params in neither of the lists
					if (paramsList != null || parentParamsList != null) {
						throw new SemantMeException(lineNumber, "The parameters of the overriden function does not match");
					}
				}
			}
		}

		///NEW!!!
		String funcNameDec = "func_" + name + ": .asciiz \"" + name + "\"\n";
		if (!sir_MIPS_a_lot.funcNames.contains(funcNameDec)){
			sir_MIPS_a_lot.funcNames += (funcNameDec);
		}


		this.st.endScope(); // in this point in time the params are not in the symbol table

		/* [9] Enter the functiom type to the symbol table */

		this.st.enter(name, function);
		/* [9] return TYPE_FUNCTION */
		return function;

	}

	/*******************Semant Body**************/
	public void SemantBody() throws SemantMeException {

		TYPE returnType = this.st.find(type);
		if (returnType == null) {
			throw new SemantMeException(lineNumber, "The return type was not de	fined");
		}
		/* [3] Begin function scope */
		this.st.beginScope();
		localVarsCounter = 0;
		this.st.setCurrnetReturnType(returnType);

		this.st.setCurrentFunctionName(name);

		//NEW!!! we already created the labek in semantSignature
		this.st.setCurrentEndLabel(endLabel);
		
		/* [4] Semant input params - we want the params */
		TYPE_LIST paramTypes = (params != null) ? params.SemantMe() : null;

		/* [7] Semant body */
		stmtList.SemantMe();

		/* [8] End scope */
		st.setCurrnetReturnType(null);

		st.setCurrentFunctionName(null);

		// OZ, New
		this.st.setCurrentEndLabel(null);

		this.st.endScope();

		this.numberOfLocals = localVarsCounter;
		localVarsCounter = -1;

	}


	/************Semant me ***********************************/
	public TYPE SemantMe() throws SemantMeException { //if f is not a method

		/* [0] Check that the return value TYPE (funcType) exists */
		TYPE returnType = this.st.find(type);
		if (returnType == null) {
			throw new SemantMeException(lineNumber, "The return type was not de	fined");
		}
		/* [1] Check that the return value is valid */
		if (!Helper.isValidType(returnType, type) && returnType != TYPE_VOID.getInstance())
		{
			throw new SemantMeException(lineNumber, "The return type is not valid");
		}
		/* [2] Check a TYPE with same name is not declared in same scope. */
		if (!this.st.IsVarNameNew(name,true)) {
			throw new SemantMeException(lineNumber, name + " was already declared");
		}

		/* [3] Begin function scope */
		this.st.beginScope();
		localVarsCounter = 0;
		this.st.setCurrnetReturnType(returnType);
		this.st.setCurrentFunctionName(name);


		/* [4] Semant input params - we want the params to be in the scope so that we can find them when we semant the body. */
		TYPE_LIST paramTypes = (params != null) ? params.SemantMe() : null;


		////NEW!! get numParams
		int numParams = getNumParams(paramTypes);

		///NEW!!
		String funcNameDec = "func_" + name + ": .asciiz \"" + name + "\"\n";
		if (!sir_MIPS_a_lot.funcNames.contains(funcNameDec)){
			sir_MIPS_a_lot.funcNames += (funcNameDec);
		}

		////

		/* [5] Check if overriding is legal */
		/* Notice that there can not be two functions in the same class with the same name (overloading is forbidden)*/
		/* Also notice that there cannot be an ovverriden function in the grandparent and one which doesn't entirley match in the parent
		* so we only need to find the first function in hierarchy that supposed to match our function */
		if (this.st.getCurrParentClass() != null) {
			TYPE_FUNCTION parentFunc = this.st.getCurrParentClass().findMethod(name);
			if (parentFunc != null) {
				// Compare return values TYPEs
				if (!Helper.compareTypes(parentFunc.returnType, returnType))
				{
					throw new SemantMeException(lineNumber, "The Overriding func return type doesn't match the overridden func return type");
				}
				// Check that the parentFunc has the same param types
				if (paramTypes != null || parentFunc.params != null) {

					TYPE_LIST paramsList = paramTypes; //Notice that we change the pinter of the list if we iterate through it.
					TYPE_LIST parentParamsList = parentFunc.params;

					while (paramsList != null && parentParamsList != null) {
						if (!Helper.compareTypes(paramsList.head, parentParamsList.head)) {
							throw new SemantMeException(lineNumber, "The parameters of the overriden function does not match");
						}
						paramsList = paramsList.tail;
						parentParamsList = parentParamsList.tail;
					}
					// Check that there are no more params in neither of the lists
					if (paramsList != null || parentParamsList != null) {
						throw new SemantMeException(lineNumber, "The parameters of the overriden function does not match");
					}
				}
			}
		}

		if ("main".equals(this.name)) { this.startLabel = "our_main"; this.isMainFunction = true;}
		else { this.startLabel = IRcommand.getFreshLabel(this.name); }

		this.endLabel = IRcommand.getFreshLabel(this.name + "_end");
		
		// OZ, New
		this.st.setCurrentEndLabel(endLabel);

		TYPE_FUNCTION function = new TYPE_FUNCTION(returnType, name, paramTypes, numParams, startLabel, endLabel);
//
		/*	Enter the function type to the symbol table for recursive calls */
		st.enter(name, function);

		/* [7] Semant body */
		stmtList.SemantMe();

		/* [8] End scope */
		st.setCurrnetReturnType(null);
		st.setCurrentFunctionName(null);
		// OZ, New
		this.st.setCurrentEndLabel(null);
		this.st.endScope();

		this.numberOfLocals = localVarsCounter;
		localVarsCounter = -1;

		st.enter(name, function);
		/* [9] return TYPE_FUNCTION */

		return function;

	}

	public static void IRPrintInt() {

		// start of function, assume params on fp + 4*k (k = 1, 2, ...)
		// previous $fp is on fp + 0 ...
		IR.getInstance().Add_IRcommand(new IRcommand_Label("PrintInt"));

        IR.getInstance().Add_IRcommand(new IR_general_command("lw $a0,4($fp)", null, false));
        IR.getInstance().Add_IRcommand(new IR_general_command("li $v0,1", null, false));
        IR.getInstance().Add_IRcommand(new IR_general_command("syscall", null, false));

        // print "space" after int
        IR.getInstance().Add_IRcommand(new IR_general_command("li $a0,32", null, false));
        IR.getInstance().Add_IRcommand(new IR_general_command("li $v0,11", null, false));
        IR.getInstance().Add_IRcommand(new IR_general_command("syscall", null, false));
		// call return address of function
		IR.getInstance().Add_IRcommand(new IR_general_jump_command());
	}

	public static void IRPrintString() {

		// start of function, assume params on fp + 4*k (k = 1, 2, ...)
		// previous $fp is on fp + 0 ...
		IR.getInstance().Add_IRcommand(new IRcommand_Label("PrintString"));

        IR.getInstance().Add_IRcommand(new IR_general_command("lw $a0,4($fp)", null, false));
        IR.getInstance().Add_IRcommand(new IR_general_command("li $v0,4", null, false));
        IR.getInstance().Add_IRcommand(new IR_general_command("syscall", null, false));
		// call return address of function
		IR.getInstance().Add_IRcommand(new IR_general_jump_command());
	}

	@Override
	public TEMP IRme() {
		Runnable irBody = () -> { stmtList.IRme(); };
		IRFuncDec(irBody, this.startLabel, this.numberOfLocals,
				this.endLabel, this.isMainFunction, true);

		return null;
	}


	public static void IRFuncDec(Runnable irBody, String startLabel, int numberOfLocals,
								 String endLabel, boolean isMainFunction, boolean isFunction) {

		int offset;
		offset = isFunction ? 1 : 0;
		IR.getInstance().Add_IRcommand(new IRcommand_Label(startLabel));

		if(isMainFunction) {
			// make sure main's fp is 0
			IR.getInstance().Add_IRcommand(new IR_general_command("addi $fp,$sp,0", null, false));
			IR.getInstance().Add_IRcommand(new IR_general_command("sw $zero,0($fp)", null, false));
		}

		// allocate place on stack for all local variables of function
		String allocate_cmd = String.format("addi $sp,$sp,%d", -4 * (numberOfLocals+offset));
		IR.getInstance().Add_IRcommand(new IR_general_command(allocate_cmd, null, false));

		irBody.run();

		// end of function, assume $v0 holds the return value (if exists)
		IR.getInstance().Add_IRcommand(new IRcommand_Label(endLabel));

		if(!isMainFunction) {
			// fold the stack
			String fold_cmd = String.format("addi $sp,$sp,%d", 4 * (numberOfLocals+offset));
			IR.getInstance().Add_IRcommand(new IR_general_command(fold_cmd, null, false));
		}

		// call return address of function
		IR.getInstance().Add_IRcommand(new IR_general_jump_command());
	}


	int getNumParams(TYPE_LIST paramTypes){

		int numParam = 0;

		for(TYPE_LIST functionParams = paramTypes; functionParams != null; functionParams = functionParams.tail) {

			numParam++;

		}
		return numParam;
	}


}
