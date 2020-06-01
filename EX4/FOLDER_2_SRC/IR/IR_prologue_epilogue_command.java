/***********/
/* PACKAGE */
/***********/
package IR;

import TEMP.*;
import MIPS.*;

public class IR_prologue_epilogue_command extends IRcommand {
	int numParams;
    boolean isRestore;

	public static final int NUM_REG = 8;
	
	public IR_prologue_epilogue_command(int numberOfParams, boolean isRestore) {
		this.numParams = numberOfParams;
        this.isRestore = isRestore;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {
        String command;
    	
    	int cmd_num = this.numParams*4 + 4; 
        if (isRestore)
            command = String.format("lw $ra,%d($fp)", cmd_num);
        else
            command = String.format("sw $ra,%d($sp)", cmd_num);
        sir_MIPS_a_lot.getInstance().runCmd(command);
        cmd_num += 4;
    	
        for(int j = 0; j < NUM_REG; j++) {
            if (isRestore)
            	command = String.format("lw $t%d,%d($fp)", j, cmd_num);
            else
                command = String.format("sw $t%d,%d($sp)", j, cmd_num);

            sir_MIPS_a_lot.getInstance().runCmd(command);
        	cmd_num+=4;
        }

        if (isRestore)
        {
            int restore_num = 4 * (1 + this.numParams+1+8);
            command = String.format("addi $sp,$fp,%d", restore_num);
            sir_MIPS_a_lot.getInstance().runCmd(command);
            command = String.format("lw $fp,0($fp)");
            sir_MIPS_a_lot.getInstance().runCmd(command);
        }
	}
}
