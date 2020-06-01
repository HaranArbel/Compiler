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

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRCommandPrintString extends IRcommand {
	TEMP t;

	public IRCommandPrintString(TEMP t) {
		this.t = t;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		sir_MIPS_a_lot.getInstance().printString(t);
	}
}
