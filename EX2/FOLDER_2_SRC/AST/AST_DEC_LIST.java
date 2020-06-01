package AST;


public class AST_DEC_LIST extends AST_DEC
{
	public AST_DEC head;
	public AST_DEC_LIST body;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST body)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== dec -> decList\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.body = body;
	}

	/************************************************/
	/* The printing message for an AST DEC node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST DEC LIST\n");

		if(head!=null) 
			head.PrintMe();
     		if(body!=null)
			body.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/


			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"Dec List");
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        	if (body != null) 
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
		

	}
}
