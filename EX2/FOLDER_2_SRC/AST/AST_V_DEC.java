package AST;

public class AST_V_DEC extends AST_VAR_DEC
{
	public String type;
	public String name;
	public AST_EXP exp;
	public AST_NEW_EXP newExp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_V_DEC(String type, String name, AST_EXP exp, AST_NEW_EXP newExp)
	{
		System.out.println("in AST_V_DEC\n");

		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp == null && newExp == null) System.out.format("====================== varDec -> ID( %s ) ID( %s );\n", type, name);
		else if (exp != null) System.out.format("====================== varDec -> ID( %s ) ID( %s ) ASSIGN exp( );\n", type, name);
		else System.out.format("====================== varDec -> ID( %s ) ID( %s ) ASSIGN newExp( );\n", type, name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type; 
		this.name = name; 
		this.exp = exp;
		this.newExp = newExp;  
	}

	/************************************************/
	/* The printing message for an INT EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE VAR_DEC\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE exp and newExp EXPRESSIONS   */
        /*********************************************/
        if (exp != null) exp.PrintMe();
        if (newExp != null) newExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
 
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		if (exp != null)
		{ 
			AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("%s %s "+":="+"exp;",type, name));
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);

		}	

		else if (newExp != null) 
		{
			AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("%s %s "+":="+"newExp;",type, name));
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
		}

		else 
		{
			AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("%s %s;",type, name));
		}
	}
}
