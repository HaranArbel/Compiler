package AST;
import SYMBOL_TABLE.*;
import TYPES.*;
import IR.*;
import TEMP.TEMP;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp or var:= newExp*/
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	public AST_NEW_EXP newExp;
	public int lineNumber;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, AST_NEW_EXP newExp, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp != null) System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");
		if (newExp != null) System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.lineNumber=lineNumber;
		if (exp != null) this.exp=exp;
		if (newExp != null) this.newExp=newExp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();
		if (newExp != null) newExp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
		if (newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
	}
    
    public TYPE SemantMe() throws SemantMeException
    {
		TYPE leftType = var.SemantMe();
		TYPE rightType;

		boolean isRightNewExp;
	
		if(exp!=null)
		{
			rightType = exp.SemantMe();
			isRightNewExp=false;
		}
		
		else
		{
			rightType = newExp.SemantMe();
			isRightNewExp=true;
		}

    	if(Helper.isAssignmentValid(leftType, rightType, isRightNewExp)) 
    		return null;
    	else
    		throw new SemantMeException(lineNumber, "Illegal Assignment");
    }
		
	//TODO, OZ, I separated two cases.
	@Override
	public TEMP IRme() {
		TEMP vp = this.var.IRme();
		TEMP ep;
		if(exp!=null)
		{
			ep = this.exp.IRme();
		}else{
			ep = this.newExp.IRme();
		}
		IR.getInstance().Add_IRcommand(new IRcommand_Store(vp, ep));
		return null;
	}
}
