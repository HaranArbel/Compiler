package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	private SYMBOL_TABLE symbolTable;
	private int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.symbolTable = SYMBOL_TABLE.getInstance();
		this.lineNumber=lineNumber;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}
    
	public TYPE SemantMe() throws SemantMeException
    {
        TYPE nameType = this.symbolTable.find(name);
		
		if (nameType != null && nameType.isInstance(name)) 
        	return nameType;
        else
            throw new SemantMeException(lineNumber, "Simple Var name must be declared, must be instance"); 
    }
	
}
