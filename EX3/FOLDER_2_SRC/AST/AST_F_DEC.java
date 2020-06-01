package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_F_DEC extends AST_DEC
{
	public String type;
	public String name;
	public AST_FUNC_DEC_PARAMS params;
	public AST_STMT_LIST stmtList;
	private SYMBOL_TABLE st;
	private int lineNumber;

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


	public TYPE SemantSignature() throws SemantMeException {
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

		this.st.endScope(); // in this point in time the params are not in the symbol table

		/* [9] Enter the functiom type to the symbol table */
		TYPE_FUNCTION function = new TYPE_FUNCTION(returnType, name, paramTypes);
		this.st.enter(name, function);
		/* [9] return TYPE_FUNCTION */
		return function;
	}


	public void SemantBody() throws SemantMeException {
		TYPE returnType = this.st.find(type);
		if (returnType == null) {
			throw new SemantMeException(lineNumber, "The return type was not de	fined");
		}
		/* [3] Begin function scope */
		this.st.beginScope();
		this.st.setCurrnetReturnType(returnType);

		/* [4] Semant input params - we want the params */
		TYPE_LIST paramTypes = (params != null) ? params.SemantMe() : null;

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

			/* [7] Semant body */
			stmtList.SemantMe();

			/* [8] End scope */
			st.setCurrnetReturnType(null);
			this.st.endScope();

	}


	public TYPE SemantMe() throws SemantMeException {

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
		this.st.setCurrnetReturnType(returnType);

		/* [4] Semant input params - we want the params to be in the scope so that we can find them when we semant the body. */
		TYPE_LIST paramTypes = (params != null) ? params.SemantMe() : null;

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

		/*	Enter the function type to the symbol table for recursive calls */
		st.enter(name, new TYPE_FUNCTION(returnType, name, paramTypes));

		/* [7] Semant body */
		System.out.println("Right before stmtList.semantMe, stmtList is null? "+ stmtList);
		stmtList.SemantMe();

		/* [8] End scope */
		st.setCurrnetReturnType(null);
		
		this.st.endScope();

		TYPE_FUNCTION function = new TYPE_FUNCTION(returnType, name, paramTypes);
		st.enter(name, function);
		/* [9] return TYPE_FUNCTION */
		return function;
	}

}
