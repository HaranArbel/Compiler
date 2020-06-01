/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;

public class IRcommandFrameBackup extends IRcommand {
	int numberOfParams;

	public static final int NUM_REG = 8;
	
	public IRcommandFrameBackup(int numberOfParams) {
		this.numberOfParams = numberOfParams;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {

    	// backup $ra
    	int i = 4 * (1 + this.numberOfParams); // offset of ra
    	String cmd = String.format("sw $ra,%d($sp)", i);
    	sir_MIPS_a_lot.getInstance().runCmd(cmd);
    	i += 4;
        
        // backup $t0,...,$t7
    	for(int j = 0; j < NUM_REG; j++) {
        	cmd = String.format("sw $t%d,%d($sp)", j, i);
			sir_MIPS_a_lot.getInstance().runCmd(cmd);
    		i += 4;
    	}
	}
}
