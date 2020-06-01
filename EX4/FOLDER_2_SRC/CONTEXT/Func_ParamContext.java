package CONTEXT;

import TEMP.*;
import MIPS.*;
import static MIPS.sir_MIPS_a_lot.WORD_SIZE;


public class Func_ParamContext extends Context {
	public int offset;
	boolean isMethod;
	
    public Func_ParamContext(int offset, boolean isMethod) {
        this.offset = offset;
        this.isMethod = isMethod;
    }

    @Override
    public void loadAddress_to_TEMP(TEMP dst, TEMP thisTmp) {
        sir_MIPS_a_lot.getInstance().loadFpAddressWithOffset(dst, calcFpOffset());
    }

    protected int calcFpOffset() {
        if (this.isMethod)  
            return (offset + 2) * WORD_SIZE;              
        return (offset + 1) * WORD_SIZE;
    }
}
