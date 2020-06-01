package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import IR.*;
import TEMP.*;

public class AST_NEW_EXP_IMPL extends AST_NEW_EXP {
	public String name;
	public AST_EXP arraySize;
	private SYMBOL_TABLE symbolTable;
	private int lineNumber;
	//new!!!
	private TYPE_CLASS classType;
 
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEW_EXP_IMPL(String name, AST_EXP size, int lineNumber)
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
		this.name = name;
		this.arraySize = size;
		
		this.symbolTable = SYMBOL_TABLE.getInstance();
		this.lineNumber = lineNumber;
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
			System.out.format("NewExp( %s [ %s ])\n",name,arraySize);
		else
			System.out.format("NewExp( %s )\n",name);
		if (arraySize != null) arraySize.PrintMe(); 
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		
		if(arraySize==null){
			AST_GRAPHVIZ.getInstance().logNode(
					SerialNumber,
					String.format("NEW\nEXP\n(%s)",name));		
		}
		else{
			AST_GRAPHVIZ.getInstance().logNode(
					SerialNumber,
					String.format("NEW\nEXP\n(%s[arraySize])",this.name));
		}
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (arraySize != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,arraySize.SerialNumber);
	}
	
	
	@Override
    public TYPE SemantMe() throws SemantMeException {

	    // [1] Make sure that type exists in Symbol Table
        TYPE varType = symbolTable.find(name);

        if (varType == null)  
			throw new SemantMeException(lineNumber, "Type: " + name + " wasn't declared");

		// [2] Make sure that type is valid
        if (!Helper.isValidType(varType, name)) 
			throw new SemantMeException(lineNumber, "Invalid var type for NEW[]");
        
		// [3] check if this is a new variable/array and return type accordingly
		if(arraySize == null)
        {
        	if (name.equals("int") || name.equals("string")){
				throw new SemantMeException(lineNumber, "int or string are not valid");
			}
			this.classType = (TYPE_CLASS) varType;
        	return varType;
        }
        else
        {
	   		// Make sure that the array size exp is of Type int
	        if (arraySize.SemantMe() != TYPE_INT.getInstance())  
				throw new SemantMeException(lineNumber, "Expected INT inside []");
	        // if passed, return the ID type (example: Father[exp] - type Father is returned e.g TYPE_CLASS)
	        return new TYPE_ARRAY(varType.name, varType);
        }

    }

    public TEMP IRme() {
    	//if array:
    	if(arraySize==null){

			AST_EXP_FUNC cc = new AST_EXP_FUNC(null, this.classType.name + "class_init", null, lineNumber);
			
			//simulate constructor with exp func call and IR
			cc.isMethod = false;
			cc.startLabel = this.classType.classInitLabel;
			return cc.IRme();
		}else
		{
			TEMP exp_t = this.arraySize.IRme();
			TEMP addr_t = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_MallocArray(addr_t, exp_t));
			return addr_t;
		}
	}
}
