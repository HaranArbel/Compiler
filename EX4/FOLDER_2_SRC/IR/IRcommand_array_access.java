package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

import static java.util.Collections.*;

public class IRcommand_array_access extends IRcommand {
    TEMP dst;
    TEMP addr_0;
    TEMP ind;

    public IRcommand_array_access(TEMP dst, TEMP addr_0, TEMP ind_0) {
        this.dst = dst;
        this.addr_0 = addr_0;
        this.ind = ind_0;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().addi(ind, ind, 1);
        TEMP t_4 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP alignedIndex = TEMP_FACTORY.getInstance().getFreshTEMP();
        sir_MIPS_a_lot.getInstance().li(t_4, 4);
        sir_MIPS_a_lot.getInstance().mul(alignedIndex, ind, t_4);
        sir_MIPS_a_lot.getInstance().add(dst, alignedIndex, addr_0);
    }
}
