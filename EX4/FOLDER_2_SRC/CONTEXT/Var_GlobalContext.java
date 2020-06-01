package CONTEXT;

import MIPS.sir_MIPS_a_lot;
import TEMP.*;

public class Var_GlobalContext extends Context {
    private String varNameLabel;

    public Var_GlobalContext(String varNameLabel) {
        this.varNameLabel = varNameLabel;
    }

    @Override
    public void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp) {
        sir_MIPS_a_lot.getInstance().la(dst, varNameLabel);
    }
}
