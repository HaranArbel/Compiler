/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;

public class IR_general_jump_command extends IRcommand {
	String label;
	TEMP addr;
	boolean isJR;
	
	public IR_general_jump_command(){
		this.isJR = true;
	}

	public IR_general_jump_command(String label) {
		this.label = label;
		this.addr = null;
		this.isJR = false;
    }
    
	public IR_general_jump_command(TEMP addr) {
		this.label = null;
		this.addr = addr;
		this.isJR = false;
    }
    
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
		if (isJR)
			sir_MIPS_a_lot.getInstance().jr();
		else{
			if (label != null) {
				sir_MIPS_a_lot.getInstance().jal(label);
			}
			else {
				sir_MIPS_a_lot.getInstance().jal(addr);
			}
		}
	}
}
