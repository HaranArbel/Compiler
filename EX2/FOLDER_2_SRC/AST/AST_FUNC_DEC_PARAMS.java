package AST;

public class AST_FUNC_DEC_PARAMS extends AST_Node
{
	public String type;
	public String name;
	public AST_FUNC_DEC_PARAMS_LIST paramsList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNC_DEC_PARAMS(String type, String name, AST_FUNC_DEC_PARAMS_LIST paramsList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (paramsList == null) System.out.format("====================== funcDecParams -> ID( %s ) ID( %s ) ();\n", type, name);
		else System.out.format("====================== funcDecParams -> ID( %s ) ID( %s ) (paramsList);\n", type, name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type; 
		this.name = name; 
		this.paramsList = paramsList;
	}

	/************************************************/
	/* The printing message for an AST FUNC DEC node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST FUNC DEC PARAMS*/
		/*******************************/
		System.out.format("AST NODE AST_FUNC_DEC_PARAMS\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE paramsList EXPRESSIONS   */
        /*********************************************/
        if (paramsList != null) paramsList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNCDEC PARAM\n%s %s",type,name));

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (paramsList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,paramsList.SerialNumber);
	}
}
