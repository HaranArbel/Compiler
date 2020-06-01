package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import IR.*;
import TEMP.*;
import MIPS.sir_MIPS_a_lot;
import CONTEXT.*;

public class AST_V_DEC extends AST_VAR_DEC
{
	public String type;
	public String name;
	public AST_EXP exp;
	public AST_NEW_EXP newExp;
	private SYMBOL_TABLE symbolTable;
	public int lineNumber;
	//added
	public Context context;
	public static int localVarsCounter = -1;


	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_V_DEC(String type, String name, AST_EXP exp, AST_NEW_EXP newExp, int lineNumber)
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
		this.symbolTable = SYMBOL_TABLE.getInstance();
		this.lineNumber = lineNumber;

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

	public TYPE SemantMe() throws SemantMeException
	{
	    // [1] Make sure that type exists in Symbol Table
		TYPE typeType = this.symbolTable.find(type);
        if(typeType == null) 
			throw new SemantMeException(lineNumber, "AST_V_DEC: type was not found in the symbol table");

        // [2] Make sure that type is valid
        if (!Helper.isValidType(typeType, type)) 
			throw new SemantMeException(lineNumber, "AST_V_DEC: type is invalid");

        // [3] Make sure that name is not already used in this scope
        if (!symbolTable.IsVarNameNew(name,false))
			throw new SemantMeException(lineNumber, "AST_V_DEC: name is already being used in this scope");

        // [4] If we're in a class that exends another class, make sure that name doesn't exist in parents
        if (symbolTable.getCurrParentClass() != null) 
		{
            if (symbolTable.getCurrParentClass().findDataMember(name) != null) 
				throw new SemantMeException(lineNumber, "AST_V_DEC: name is already a name of a data member in one of the class parents");
			// [4.5] If we're in a class that exends another class, make sure that name of var is not a func in parent
			if (symbolTable.getCurrParentClass().findMethod(name) != null)
				throw new SemantMeException(lineNumber, "AST_V_DEC: name is already a name of a method in one of the class parents");
        }

        // [5] Make sure that expression type assignment is valid
        if(exp != null)
        {
            if (!Helper.isAssignmentValid(typeType, exp.SemantMe(), false))
				throw new SemantMeException(lineNumber,"Illegal assignment");
        }
        else if(newExp != null)
        {
        	TYPE val = newExp.SemantMe();
			if (!Helper.isAssignmentValid(typeType, val, true)) {
				throw new SemantMeException(lineNumber, "Illegal assignment");
			}
        }

        // create context for curr var
        if (AST_F_DEC.localVarsCounter >= 0) {
            this.context = new Func_LocalContext(AST_F_DEC.localVarsCounter++);
        } else if (AST_DEC_CLASSDEC.fieldsCounter >= 0) {
            this.context = new Var_ClassContext(AST_DEC_CLASSDEC.fieldsCounter++);
        } else {
            final String labelName = "global_var_" + name;
            this.context = new Var_GlobalContext(labelName);
            sir_MIPS_a_lot.addGlobalVariable(labelName);
        }
        
        symbolTable.enter(name, typeType, this.context);		
		// [6] enter the type of the declared variable to he symbol table
		return null;
    }

    public TEMP IRme() {
        if (this.exp==null && newExp==null) {return null;}
		TEMP address = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Load(address, null, 0, context,true ));
        TEMP fresh;
        if (this.exp != null) 
            fresh = exp.IRme();
        else
       		fresh = newExp.IRme();
       	IR.getInstance().Add_IRcommand(new IRcommand_Store(address, fresh));
        //}

        return null;
    }
}
