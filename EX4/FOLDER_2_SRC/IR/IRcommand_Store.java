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

public class IRcommand_Store extends IRcommand {
	
	TEMP dst;
	TEMP src;
	int offset; 
	Boolean withOffset;


	public IRcommand_Store(TEMP dst, TEMP src, int offset, Boolean wOffset){
		this.dst = dst;
		this.src = src;
		this.offset = offset; 
		this.withOffset = wOffset;
	}

	public IRcommand_Store(TEMP dst, int offset) {
        this(dst, null, offset, true);
    }
	
	public IRcommand_Store(TEMP dst, TEMP src) {
		this(dst, src, -1, false);
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		if (this.withOffset)
			sir_MIPS_a_lot.getInstance().storeToFpAddressWithOffset(dst, offset);
		else
			sir_MIPS_a_lot.getInstance().store(dst,src);
	}
}