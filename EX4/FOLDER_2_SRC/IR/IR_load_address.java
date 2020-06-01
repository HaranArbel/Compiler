package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IR_load_address extends IRcommand {
    TEMP dst;
    String label;

    public IR_load_address(TEMP dst, String label) {
        this.dst = dst;
        this.label = label;
    }

    @Override
    public void MIPSme() {
        sir_MIPS_a_lot.getInstance().la(dst, label);
    }
}
