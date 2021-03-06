package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import CONTEXT.*;
import TEMP.*;
import IR.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	public int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
		this.lineNumber=lineNumber;
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
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}
	
	
    public TYPE SemantMe() throws SemantMeException 
    {
        TYPE varType = null;
        TYPE_CLASS varClassType = null;
        
        /******************************/
        /* [1] Recursively semant var */
        /******************************/
        if (var != null) varType = var.SemantMe();
        
        /*********************************/
        /* [2] Make sure type is a class */
        /*********************************/
        if (!varType.isClass())
            throw new SemantMeException(lineNumber, "access %s field of a non-class variable");
        else
        	varClassType = (TYPE_CLASS) varType;
        
        /************************************/
        /* [3] Look for fiedlName inside tc */
        /************************************/
        /// OZ, I changed the findDataMember in order to returm TYPE_CLASS_VAR_DEC.
        // Before we returned TYPE_CLASS_VAR_DEC.t, and we could no extract the context  
        TYPE dataMember = varClassType.findDataMember(fieldName, false, true);
        if ( dataMember != null) {
			this.context = ((TYPE_CLASS_VAR_DEC) dataMember).context;
        	return ((TYPE_CLASS_VAR_DEC) dataMember).t;
        }        
       
        /*********************************************/
        /* [4] fieldName does not exist in class var */
        /*********************************************/
		//If we reached this point throw an exception
        throw new SemantMeException(lineNumber, "data member does not exist in class");
    }

	public TEMP IRme() 
	{
        TEMP varAddres = this.var.IRme();
        TEMP res = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Load(res, varAddres, 0, this.context, true));      
        return res;
    }
}
