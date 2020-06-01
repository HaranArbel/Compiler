package IR;

import MIPS.sir_MIPS_a_lot;
import TEMP.TEMP;

public class IRcommand_Runtime_error extends IRcommand {

    String label;

    public IRcommand_Runtime_error(String const_label) {
        this.label = const_label;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {

        sir_MIPS_a_lot.getInstance().print_string_by_label(label);
        sir_MIPS_a_lot.getInstance().exitProgram();
    }
}
