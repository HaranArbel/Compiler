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
import TEMP.*;

public class IRCommandPrintTrace extends IRcommand {
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme() {

		/*******************************/
		/* [1] Allocate 2 fresh temps  */
		/*******************************/
        TEMP funcName = TEMP_FACTORY.getInstance().getFreshTEMP();
	    TEMP fpAddress = TEMP_FACTORY.getInstance().getFreshTEMP();

		/*******************************/
		/* [2] Allocate 2 fresh labels */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("WHILE_END");
		String label_start = IRcommand.getFreshLabel("WHILE_START");

		/*******************************/
		/* [3] get Frame Pointer addr  */
		/*******************************/
        sir_MIPS_a_lot.getInstance().loadLWFpAddressWithOffset(fpAddress, 0);

		/*******************************/
		/* [4] while cond              */
		/*******************************/            
        new IRcommand_Label(label_start).MIPSme();

		/******************************************/
		/* [5] Jump conditionally to the loop end */
		/******************************************/
        new IRcommand_Jump_If_Eq_To_Zero(fpAddress, label_end).MIPSme();
        
		/**********************/
		/* [6] print funcName */
		/**********************/            
        new IRcommand_Load(funcName, fpAddress, -4, null, false).MIPSme();
        new IRCommandPrintString(funcName).MIPSme();

		/**********************/
		/* [7] load prev fp   */
		/**********************/            
        new IRcommand_Load(fpAddress, fpAddress, 0, null, false).MIPSme();

		/******************************/
		/* [8] Jump to the loop entry */
		/******************************/
		new IRcommand_Jump_Label(label_start).MIPSme();

	    /**********************/
		/* [9] Loop end label */
		/**********************/
		new IRcommand_Label(label_end).MIPSme();

	    /*******************************/
		/* [10] hardcoded print "main" */
		/*******************************/
	    new IR_general_command("la $a0,func_main", null, false).MIPSme();

	    new IR_general_command("li $v0,4", null, false).MIPSme();
	    new IR_general_command("syscall", null, false).MIPSme();
    }
}
