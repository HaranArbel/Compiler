package AST;

public class AST_FUNC_DEC_PARAMS_LIST extends AST_Node
{
	public String type;
	public String name;
	public AST_FUNC_DEC_PARAMS_LIST body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNC_DEC_PARAMS_LIST(String type, String name, AST_FUNC_DEC_PARAMS_LIST body)
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
		this.body = body;
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
        if (body != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNCDEC PARAMLIST\n%s %s",type,name));

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
	if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
}
