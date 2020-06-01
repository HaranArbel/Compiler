package AST;

public class AST_A_DEC extends AST_DEC 
{
	public String type;
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_A_DEC(String type, String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== arrayDec -> ARRAY ID( %s ) = ID( %s ) [ ];\n", name, type); 

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type; 
		this.name = name; 
	}

	/************************************************/
	/* The printing message for an ARRAY DECLARATION AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = ARRAY DECLARATION AST */
		/*******************************/
		System.out.format("AST NODE ARRAY_DEC\n");

		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ARRAY ID(%s)  = ID(%s)[]",name,type));

	}
}
