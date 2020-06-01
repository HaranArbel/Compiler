package AST;

public class AST_EXP_FUNC extends AST_EXP
{

	public AST_VAR var;
	public String id;
	public AST_EXP_PARAMS params;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_FUNC(AST_VAR var,  String id, AST_EXP_PARAMS params)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/

		if (var != null && params != null) System.out.format("====================== exp -> var.%s(exp list)\n", id);
		else if (var == null && params != null) System.out.format("====================== exp -> %s(exp list)\n", id);
		else if (var != null && params == null) System.out.format("====================== exp -> var.%s()\n", id);
		else System.out.format("====================== exp -> %s()\n", id);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.params = params;
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

}
