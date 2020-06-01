package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;

public class AST_FUNC_DEC_PARAMS_LIST extends AST_Node
{
	public String type;
	public String name;
	public AST_FUNC_DEC_PARAMS_LIST tail;
	public int lineNumber;
	private SYMBOL_TABLE symbolTable;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNC_DEC_PARAMS_LIST(String type, String name, AST_FUNC_DEC_PARAMS_LIST tail, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== funcDecParamsList -> COMMA ID( %s ) ID( %s ) (paramsList);\n", type, name); /* always contains one element at least*/

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type; 
		this.name = name; 
		this.tail = tail;
		this.lineNumber = lineNumber;
		this.symbolTable = SYMBOL_TABLE.getInstance();
	}

	/************************************************/
	/* The printing message for an AST FUNC DEC node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST FUNC DEC PARAMS*/
		/*******************************/
		System.out.format("AST NODE AST_FUNC_DEC_PARAMS_LIST\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE paramsList EXPRESSIONS   */
        /*********************************************/
        if (tail != null) tail.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNCDEC PARAMLIST\n%s %s",type,name));

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
	if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}


	public TYPE_LIST SemantMe() throws SemantMeException {

		// [1] Make sure that type exists in Symbol Table 
		TYPE t = this.symbolTable.find(type);
		if (t == null){
			throw new SemantMeException(lineNumber, "This type wasn't declared: " + type);
		}

		// [2] Make sure that type is valid
		if (!Helper.isValidType(t, type)) {
			throw new SemantMeException(lineNumber, "Invalid param type");
		}
		
		// [3] Make sure that name is not already used in this scope
		if (!this.symbolTable.IsVarNameNew(name,false)) {
			throw new SemantMeException(lineNumber, "Parameter with the same name was already declared(" + name + ")");
		}

		// [4] enter this parameter to the symbol table
		this.symbolTable.enter(name, t);

		// [5] Semant the rest of the paramsList
		TYPE_LIST paramsTypesList = new TYPE_LIST(t, null);
		if (tail != null)
		{
			paramsTypesList.tail = tail.SemantMe();
		}
		
		return paramsTypesList;
		
	}
}
