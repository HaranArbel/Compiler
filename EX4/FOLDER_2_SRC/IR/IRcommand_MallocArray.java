package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;
import TEMP.TEMP_FACTORY;

public class IRcommand_MallocArray extends IRcommand {

    TEMP size;
    TEMP dst;

    public IRcommand_MallocArray(TEMP dst, TEMP size) {
        this.size = size;
        this.dst = dst;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {

        TEMP newSize = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().addi(newSize, size, 1);
        sir_MIPS_a_lot.getInstance().malloc(dst, newSize);
        
        // storing the size of the array
        sir_MIPS_a_lot.getInstance().store(dst, size);
    }

}
