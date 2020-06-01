package AST;

public class AST_NEW_EXP_IMPL extends AST_NEW_EXP {
	public String newobjName;
	public AST_EXP arraySize;
	

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEW_EXP_IMPL(String name, AST_EXP size)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if(arraySize!=null)
			System.out.format("====================== newExp -> NEW ID( %s [ %s ] )\n",name, size);
		else
			System.out.format("====================== newExp -> NEW ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.newobjName = name;
		this.arraySize = size;
	}


	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (arraySize!=null) 
			System.out.format("NewExp( %s [ %s ])\n",newobjName,arraySize);
		else
			System.out.format("NewExp( %s )\n",newobjName);
		if (arraySize != null) arraySize.PrintMe(); 
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		
		if(arraySize==null){
			AST_GRAPHVIZ.getInstance().logNode(
					SerialNumber,
					String.format("NEW\nEXP\n(%s)",newobjName));		
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
					SerialNumber,
					String.format("NEW\nEXP\n(%s[arraySize])",newobjName));
		}
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (arraySize != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,arraySize.SerialNumber);
	}

	
}
