package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Check_For_Null_Pointer_Dereference extends IRcommand {

    private TEMP thisPointer;

    public IRcommand_Check_For_Null_Pointer_Dereference(TEMP thisPointer) {
        this.thisPointer = thisPointer;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().checkPointerDereference(thisPointer);
    }
}
