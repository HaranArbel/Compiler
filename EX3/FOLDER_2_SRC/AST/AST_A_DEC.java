package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;


public class AST_A_DEC extends AST_DEC 
{
	public String type;
	public String name;
	public int lineNumber;
	private SYMBOL_TABLE symbolTable;
	 
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_A_DEC(String name, String type, int lineNumber)
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
		this.lineNumber = lineNumber;
		this.symbolTable = SYMBOL_TABLE.getInstance();  
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

	
	public TYPE SemantMe() throws SemantMeException
	{	
		//make sure that array name is valid
		if(!this.symbolTable.IsVarNameNew(name,true))
		{
			throw new SemantMeException(lineNumber,"The name "+name+" already exists in the symbol table!\n");
		}

		//make sure that array type exists in symbol table
		TYPE arrayType = this.symbolTable.find(type);
		if(arrayType==null)
		{
			throw new SemantMeException(lineNumber,"The requested array type doesn't exist in the symbol table\n");
		}
		
		//make sure that type is either class/array/int/string
		if (!Helper.isValidType(arrayType,type))
		{
			throw new SemantMeException(lineNumber,"The requested array type is not valid for creating an array\n");
		}
						
		//add this array to symbol table
		this.symbolTable.enter(name, new TYPE_ARRAY(name,arrayType));
		
		//reutrned value from AST_A_DEC is not important
		return null;

	}
}
