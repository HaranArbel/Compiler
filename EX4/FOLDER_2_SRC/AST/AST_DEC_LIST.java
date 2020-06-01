package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TYPES.*;
import TEMP.*;


public class AST_DEC_LIST extends AST_DEC
{
	public AST_DEC head;
	public AST_DEC_LIST tail;
	private int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail , int lineNumber)
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
		this.tail = tail;
		this.lineNumber=lineNumber;
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
     		if(tail!=null)
			tail.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/


			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"Dec List");
		if (head != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        	if (tail != null)
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
		
	}
    public TYPE SemantMe() throws SemantMeException
    {
        if (head != null) head.SemantMe();
        if (tail != null) tail.SemantMe();
        
        return null;
    }

	public TEMP IRme() {
		if (this.head != null) this.head.IRme();
		if (this.tail != null) this.tail.IRme();
		return null;
	}
}
