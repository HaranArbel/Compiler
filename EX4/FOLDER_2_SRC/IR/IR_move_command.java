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

import TEMP.*;
import MIPS.*;

public class IR_move_command extends IRcommand {
    TEMP temp_src;
    TEMP temp_dst;
    String string_dst;
    String string_src;

    public IR_move_command(String string_dst, TEMP temp_src, TEMP temp_dst, String string_src) {
        if (string_dst != null){
            this.temp_src = temp_src; 
            this.string_dst = string_dst;
        }
        else{
            if (temp_dst != null){
                this.temp_dst = temp_dst;
                this.string_src = string_src;
            }
        }
       
    }

    // public IR_move_command(TEMP dst, String src) {
    //     this.temp_src = null;
    //     this.temp_dst = dst;
    //     this.string_src = src;
    //     this.string_dst = null;
    // }






    /***************/
    /* MIPS me !!! */
    /***************/
    
    public void MIPSme() {
        if (temp_src != null && string_dst != null) {
            sir_MIPS_a_lot.getInstance().move(string_dst, temp_src);
        } else if (temp_dst != null && string_src != null) {
            sir_MIPS_a_lot.getInstance().move(temp_dst, string_src);
        }
    }
}
