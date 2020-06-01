package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IR_branch_less extends IRcommand {
    TEMP oper1;
    TEMP oper2;
    String label;
    Boolean equal;

    public IR_branch_less(TEMP oper1, TEMP oper2 , String label, boolean eq) {
        this.oper1 = oper1;
        this.oper2 = oper2;
        this.label = label;
        this.equal = eq;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {
        if (this.equal)
            sir_MIPS_a_lot.getInstance().ble(oper1, oper2, label);
        sir_MIPS_a_lot.getInstance().blt(oper1, oper2, label);
    }
}
