package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;

public class AST_EXP_PARAMS_LIST extends AST_Node
{
	public AST_EXP head;
	public AST_EXP_PARAMS_LIST tail;
	public int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_PARAMS_LIST(AST_EXP head,AST_EXP_PARAMS_LIST tail, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.format("======================  paramList -> COMMA exp exp (paramsList);\n"); /* always contains one element at least*/
		else System.out.format("======================  paramList -> COMMA exp exp;	\n");
		
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head; 
		this.tail = tail;
		this.lineNumber=lineNumber;
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
		if (tail != null) tail.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNC PARAMLIST"));

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
        
 		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);       
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}
	
    public TYPE_LIST SemantMe() throws SemantMeException
    {
		TYPE_LIST paramsTypesList = new TYPE_LIST(head.SemantMe(), null);
		if (tail!=null) 
		{
			paramsTypesList.tail = tail.SemantMe();
		}
		return paramsTypesList;
    }
	@Override
	public TEMP IRme() {
		return head.IRme();
	}
}
