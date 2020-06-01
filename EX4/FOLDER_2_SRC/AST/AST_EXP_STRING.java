package AST;
import SYMBOL_TABLE.SYMBOL_TABLE;
import TYPES.*;
import java.util.*;
import IR.*;
import TEMP.*;

public class AST_EXP_STRING extends AST_EXP
{
	private static List<String> strings = new ArrayList<>();
	public String str_label;
	public String str;
	private int lineNumber;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_STRING(String str, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> STERING( %s )\n", str);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.str = str;
		this.lineNumber=lineNumber;
	}

	/************************************************/
	/* The printing message for an STRING EXP AST node */
	/************************************************/
	public void PrintMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST STRING EXP */
		/*******************************/
		System.out.format("AST NODE STRING( %s )\n",str);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STRING",str));
	}
	
	public TYPE SemantMe() throws SemantMeException{
        
		if (this.str.charAt(0) == '"' && this.str.charAt(this.str.length()-1) == '"') {
			this.str = this.str.substring(1, this.str.length()-1);
		}
		this.str_label = getStringLabel(strings.size());
		strings.add(this.str);

        return TYPE_STRING.getInstance();
	}

    public static List<String> allStrings() {
        return strings;
    }

    public static String getStringLabel(int i) {
        return String.format("string_%d", i);
    }
	
	public boolean isConst() {
		return true;
	}

    public TEMP IRme() {
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IR_load_address(t, this.str_label));
        return t;
	}
	
}
