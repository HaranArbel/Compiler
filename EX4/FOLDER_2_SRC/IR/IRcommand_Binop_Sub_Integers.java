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

public class IRcommand_Binop_Sub_Integers extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;
	
	public IRcommand_Binop_Sub_Integers(TEMP dst,TEMP t1,TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
        sir_MIPS_a_lot.getInstance().sub(dst, t1, t2);

		TEMP intMin = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().li(intMin, -32767);
        String label_underflow = getFreshLabel("UNDERFLOW");
        sir_MIPS_a_lot.getInstance().blt(dst, intMin, label_underflow);
 
 	    TEMP intMax = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().li(intMax, 32767);
        String label_overflow = getFreshLabel("OVERFLOW");
        sir_MIPS_a_lot.getInstance().blt(intMax,dst, label_overflow);

		String label_end = getFreshLabel("SUB_END");	
		sir_MIPS_a_lot.getInstance().jump(label_end);

        sir_MIPS_a_lot.getInstance().label(label_underflow);
        sir_MIPS_a_lot.getInstance().li(dst, -32768);
        sir_MIPS_a_lot.getInstance().jump(label_end);

        sir_MIPS_a_lot.getInstance().label(label_overflow);
        sir_MIPS_a_lot.getInstance().li(dst, 32767);

        sir_MIPS_a_lot.getInstance().label(label_end);
	}
}
