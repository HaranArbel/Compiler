package AST;

public class AST_EXP_PARAMS_LIST extends AST_Node
{
	public AST_EXP head;
	public AST_EXP_PARAMS_LIST body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_PARAMS_LIST(AST_EXP head,AST_EXP_PARAMS_LIST body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (body != null) System.out.format("======================  paramList -> COMMA exp exp (paramsList);\n"); /* always contains one element at least*/
		else System.out.format("======================  paramList -> COMMA exp exp;	\n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head; 
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
		System.out.format("AST NODE AST_EXP_PARAMS_LIST\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE paramsList EXPRESSIONS   */
        /*********************************************/
        if (head!=null) head.PrintMe();
		if (body != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNC PARAMLIST"));

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
        
 		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);       
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
}
