package AST;

public class AST_F_DEC extends AST_DEC
{
	public String type;
	public String name;
	public AST_FUNC_DEC_PARAMS params;
	public AST_STMT_LIST stmtList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_F_DEC(String type, String name, AST_FUNC_DEC_PARAMS params, AST_STMT_LIST stmtList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (params == null) System.out.format("====================== funcDec -> ID( %s ) ID( %s ) () {stmtList} ;\n", type, name);
		else System.out.format("====================== funcDec -> ID( %s ) ID( %s ) (params) {stmtList};\n", type, name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type; 
		this.name = name; 
		this.params = params;
		this.stmtList = stmtList;  
	}

	/************************************************/
	/* The printing message for an AST FUNC DEC node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST FUNC DEC */
		/*******************************/
		System.out.format("AST NODE FUNC_DEC\n");

		/*********************************************/
		/* RECURSIVELY PRINT THE params and stmtList EXPRESSIONS   */
        /*********************************************/
        if (params != null) params.PrintMe();
        if (stmtList != null) stmtList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/

	if(params!=null&&stmtList!=null)
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s(params) {stmtList}",type,name));
	}
	else if(params==null&&stmtList==null)
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s() {}",type,name));
	}
	else if(params==null)
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s() {stmtList}",type,name));
	}
	else
	{
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s %s() {params}",type,name));
	}


		/*********************************/
		/* Print to AST GRAPHIZ DOT file */   
		/*********************************/
	if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
	if (stmtList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,stmtList.SerialNumber);
	}
}
