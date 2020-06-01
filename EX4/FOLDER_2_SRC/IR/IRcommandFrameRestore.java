/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;

public class IRcommandFrameRestore extends IRcommand {
	int numberOfParams;

	public static final int NUM_REG = 8;
	
	public IRcommandFrameRestore(int numberOfParams) {
		this.numberOfParams = numberOfParams;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {

    	// restore $ra
    	int i = 4 * (1 + this.numberOfParams); // offset of ra
        String cmd = String.format("lw $ra,%d($fp)", i);
        sir_MIPS_a_lot.getInstance().runCmd(cmd);
        i += 4;
    	
    	// restore $t0,...,$t7
        for(int j = 0; j < NUM_REG; j++) {
        	cmd = String.format("lw $t%d,%d($fp)", j, i);
            sir_MIPS_a_lot.getInstance().runCmd(cmd);
    		i+=4;
        }
        
        // restore $sp
        int prologSize = 4 * (1 + this.numberOfParams+1+8);
        cmd = String.format("addi $sp,$fp,%d", prologSize);
        sir_MIPS_a_lot.getInstance().runCmd(cmd);

        // restore $fp from (next-fp)+0
        cmd = String.format("lw $fp,0($fp)");
        sir_MIPS_a_lot.getInstance().runCmd(cmd);
	}
}
