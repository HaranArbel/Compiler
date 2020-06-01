package CONTEXT;

import IR.*;
import TEMP.*;
import MIPS.sir_MIPS_a_lot;

public class Var_ClassContext extends Context {
    public int offset;

    public Var_ClassContext(int offset) {
        this.offset = offset;
    }

    @Override
    public void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp) {

        TEMP pthis = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (thisTmp == null) {

            // this is a simple var, means we're inside a function
            // method, so the pointer for this is inside the stack
            String cmd = "lw $t%d, 4($fp)"; // place of "this"
            sir_MIPS_a_lot.getInstance().runCmd(cmd, pthis.getSerialNumber(), true);
        } else {

            // this is a field func call, load the address of the object
            sir_MIPS_a_lot.getInstance().load(pthis, thisTmp);
            sir_MIPS_a_lot.getInstance().checkPointerDereference(pthis);
        }
        sir_MIPS_a_lot.getInstance().addi(dst, pthis, (offset + 1) * 4);
    }

}
