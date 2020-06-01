package AST;

public class AST_DEC_CLASSDEC extends AST_DEC
{

    public String parentName;
    public String idName;   
    public AST_C_FIELD_LIST fields;
    
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_CLASSDEC(String idName,  String parentName, AST_C_FIELD_LIST fields )
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> classDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.parentName = parentName;
		this.idName = idName;
		this.fields = fields;
	}
	
	/***********************************************/
	/* The default message for an dec classDec AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = DEC CLASSDEC AST NODE */
		/************************************/
		System.out.print("AST NODE DEC CLASSDEC\n");

		/*****************************/
		/* RECURSIVELY PRINT classDec ... */
		/*****************************/
		if (fields != null) fields.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (parentName!=null)
		{		
			AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("CLASS %s EXTENDS %s",idName,parentName));
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				String.format("CLASS %s",idName));

		}
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
	
		if (fields != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fields.SerialNumber);
			
	}
}
