package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;
import IR.*;

public class AST_EXP_BINOP extends AST_EXP
{
	public int OP;
	public AST_EXP left;
	public AST_EXP right;
	public int lineNumber;
	private boolean stringsOperator = false;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.lineNumber=lineNumber;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}
	
		
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}
	

    public TYPE SemantMe() throws SemantMeException
    {    	
        TYPE typeLeft = left.SemantMe();
        TYPE typeRight = right.SemantMe();

        if ((typeLeft == TYPE_STRING.getInstance()) && (typeRight == TYPE_STRING.getInstance())){
        	this.stringsOperator = true;
        } 
        
        if (OP==0) { // operation + can only be between two ints or two strings
        	if ((typeLeft == TYPE_STRING.getInstance()) && (typeRight == TYPE_STRING.getInstance())){
        		return TYPE_STRING.getInstance();
        	}
        	else if ((typeLeft == TYPE_INT.getInstance()) && (typeRight == TYPE_INT.getInstance()))
        		return TYPE_INT.getInstance();
        	else
            	throw new SemantMeException(lineNumber, "Plus Binary Operation is only between two ints/strings");
        }
        
        else if( OP == 1 || OP == 2 || OP == 3 || OP == 4 || OP == 5  ) {
        	if (typeLeft == TYPE_INT.getInstance() && typeRight == TYPE_INT.getInstance()) {
               return TYPE_INT.getInstance();
            } else {
                throw new SemantMeException(lineNumber, "- * / < > Binary Operations are only between two ints");
            }
        }
        
        else if (OP == 6) { //equality check
            if (Helper.isEqualityValid(typeLeft, typeRight))
            	return TYPE_INT.getInstance();
            else
            	throw new SemantMeException(lineNumber, "Illegal Equality Check");
        }
               
        else 
            throw new SemantMeException(lineNumber, "UNKOWN BINOP");
    }
	
	public TEMP IRme()
	{
		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
				
		if (left  != null) t1 = left.IRme();
		if (right != null) t2 = right.IRme();
		
		if (OP == 0)
		{
			if (!this.stringsOperator) {
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
			}
			else {
				IR.
				getInstance().
				Add_IRcommand(new IR_string_binop(dst, t1, t2,true));
			}
		}
		if (OP == 1)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
		}

		if (OP == 2)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
		}
		if (OP == 3)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_Div_Integers(dst,t1,t2));
		}
		if (OP == 4)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		if (OP == 5)
		{
			IR.
			getInstance().
			Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
		}
		if (OP == 6)
		{
			if (!this.stringsOperator) {
				IR.
				getInstance().
				Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst,t1,t2));
			}
			else {
				IR.
				getInstance().
				Add_IRcommand(new IR_string_binop(dst, t1, t2,false));
			}
		}

		return dst;
	}  
}

