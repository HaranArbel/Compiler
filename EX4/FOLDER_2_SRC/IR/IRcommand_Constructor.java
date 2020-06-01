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

import MIPS.sir_MIPS_a_lot;
import TEMP.*;
import TYPES.*;

public class IRcommand_Constructor extends IRcommand {
    private String endLabel;
    private TYPE_CLASS typeClass;

    public IRcommand_Constructor(String endLabel, TYPE_CLASS typeClass) {
        this.endLabel = endLabel;
        this.typeClass = typeClass;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme() {

        boolean contains_functions;
        TEMP size = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP instance_ptr = TEMP_FACTORY.getInstance().getFreshTEMP();

        // add 1 for vTable
        int vars_size = typeClass.varInitValues.size();
        sir_MIPS_a_lot.getInstance().li(size, 4*(vars_size+1));
        // allocate space for vTable
        sir_MIPS_a_lot.getInstance().malloc(instance_ptr, size, false);

        if  (!sir_MIPS_a_lot.vtables.get(typeClass.vTableLabel).isEmpty()){
            contains_functions = true;
        }
        else{
            contains_functions= false;
        }
 
        if (contains_functions) {
            TEMP vtableAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
            sir_MIPS_a_lot.getInstance().la(vtableAddress, typeClass.vTableLabel);
            sir_MIPS_a_lot.getInstance().store(instance_ptr, vtableAddress);
        }

        if (!typeClass.varInitValues.isEmpty()) {
            TEMP memberP = TEMP_FACTORY.getInstance().getFreshTEMP();
            sir_MIPS_a_lot.getInstance().addi(memberP, instance_ptr, 4);
            TEMP tmp = TEMP_FACTORY.getInstance().getFreshTEMP();

            TEMP varInitLabelAddress = TEMP_FACTORY.getInstance().getFreshTEMP();
            sir_MIPS_a_lot.getInstance().la(varInitLabelAddress, typeClass.varInitLabel);
            for (int i = 0; i < typeClass.varInitValues.size(); i++) {
                sir_MIPS_a_lot.getInstance().load(tmp, varInitLabelAddress, i * 4);

                sir_MIPS_a_lot.getInstance().store(memberP, tmp);
                sir_MIPS_a_lot.getInstance().addi(memberP, memberP, 4);
            }
        }

        sir_MIPS_a_lot.getInstance().move("$v0", instance_ptr); // return register
        sir_MIPS_a_lot.getInstance().jump(endLabel);

    }
}
