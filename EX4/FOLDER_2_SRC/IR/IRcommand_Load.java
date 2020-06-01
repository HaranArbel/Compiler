/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;
import CONTEXT.*;


public class IRcommand_Load extends IRcommand {

	TEMP dst;
	TEMP src;
	int offset;
	Context context;
	boolean loadVarAddress;
	
	public IRcommand_Load(TEMP dst, TEMP src) {
		this(dst, src, 0, null, false);
	}

	public IRcommand_Load(TEMP dst, TEMP src, int offset, Context cntx, boolean loadVar) {
		this.dst = dst;
		this.src = src;
		this.offset = offset;
		this.context = cntx;
		this.loadVarAddress = loadVar;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		if (loadVarAddress)
			sir_MIPS_a_lot.getInstance().loadAddressVar(dst, context, src);
		else 
			sir_MIPS_a_lot.getInstance().load(dst, src, offset);
	}
}
