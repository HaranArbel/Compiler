package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import TEMP.*;

	public class AST_EXP_PARAMS extends AST_Node
	{
		public AST_EXP head;
		public AST_EXP_PARAMS_LIST paramsList;
		public int lineNumber;
		
		/******************/
		/* CONSTRUCTOR(S) */
		/******************/
		public AST_EXP_PARAMS(AST_EXP exp, AST_EXP_PARAMS_LIST list, int lineNumber)
		{
			/******************************/
			/* SET A UNIQUE SERIAL NUMBER */
			/******************************/
			SerialNumber = AST_Node_Serial_Number.getFresh();

			/***************************************/
			/* PRINT CORRESPONDING DERIVATION RULE */
			/***************************************/
			if (paramsList == null) System.out.format("====================== paramlist -> null;\n");
			else System.out.format("====================== paramlist -> param paramlist;\n");

			/*******************************/
			/* COPY INPUT DATA NENBERS ... */
			/*******************************/
			this.head = exp;  
			this.paramsList = list;
			this.lineNumber=lineNumber;
		}

		/************************************************/
		/* The printing message for an AST FUNC DEC node */
		/************************************************/
		public void PrintMe()
		{
			/*******************************/
			/* AST NODE TYPE = AST FUNC DEC PARAMS*/
			/*******************************/
			System.out.format("AST NODE AST_EXP_PARAMS\n");

			/*********************************************/
			/* RECURSIVELY PRINT THE paramsList EXPRESSIONS   */
			/*********************************************/
			if (head!=null) head.PrintMe();
			if (paramsList != null) paramsList.PrintMe();

			/***************************************/
			/* PRINT Node to AST GRAPHVIZ DOT file */
			/***************************************/
			AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNC PARAM"));

			/*********************************/
			/* Print to AST GRAPHIZ DOT file */
			/*********************************/
						
			if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
			if (paramsList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,paramsList.SerialNumber);
		}


        public TYPE_LIST SemantMe () throws SemantMeException
		{
			TYPE_LIST paramsTypesList = new TYPE_LIST(head.SemantMe(), null);
			if (paramsList!=null) 
			{
				paramsTypesList.tail = paramsList.SemantMe();
			}
			return paramsTypesList;
        }

		@Override
		public TEMP IRme() {
			return head.IRme();
		}
	}
