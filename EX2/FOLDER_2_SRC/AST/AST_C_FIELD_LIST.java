package AST;

public class AST_C_FIELD_LIST extends AST_Node
{
	public AST_C_FIELD head;
	public AST_C_FIELD_LIST tail;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD_LIST(AST_C_FIELD head, AST_C_FIELD_LIST tail) {
		
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.format("====================== cField cFieldList\n");
		else System.out.format("====================== cField \n");
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head; 
		this.tail = tail;	
	}
	
	
	/************************************************/
	/* The printing message for an AST_C_FIELD_LIST as node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST CFIELD FUNCDEC */
		/*******************************/
		System.out.format("AST NODE C_FIELD_LIST\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE fDec EXPRESSIONS   */
        	/*********************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();
		
		if (head != null) AST_GRAPHVIZ.getInstance().logNode(SerialNumber,"cField\nList");
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
}

	

