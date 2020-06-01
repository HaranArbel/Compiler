/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.*;
import static java.util.Collections.emptySet;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import AST.*;
import CONTEXT.*;
import TEMP.*;
import CONTROL_FLOW.*;
import IR.IRcommand;


public class sir_MIPS_a_lot {


    public static final String POINTER_DEREFERENCE = "string_invalid_ptr_dref";
    
    public static Map<String,List<String>> vtables = new HashMap<>(); // class label -> vTable
    private static List<String> globalVars = new ArrayList<>();
    public static Map<String, List<String>> classInitValues = new HashMap<>(); // var label -> list of initial values
    public static String funcNames = "";

    public static int WORD_SIZE = 4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;
    private List<CFG_Node> commandList;
    private Map<String, CFG_Node> labelToNodeMap;
    private boolean dontAddEdgeForNextCommand;
	public boolean fileCreated = false;
	
	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalize() { 
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	
    public void exitProgram() {
        append_cmd("\tli $v0,10\n\tsyscall\n");
    }
	
	
	// maybe should be la instead of move
	public void printString(TEMP t) {
		int idx = t.getSerialNumber();
		String cmd = "\tmove $a0,$t%d\n\tli $v0,4\n\tsyscall\n";

		append_cmd(cmd, emptySet(), createSet(idx), idx);
	}
	
    public void print_string_by_label(String label) {
        append_cmd(String.format("\tla $a0,%s\n", label)
                + "\tli $v0,4\n\tsyscall\n");
    }
    
	public void loadAddressVar(TEMP dst, Context context, TEMP thisTmp) {
        context.loadAddress_to_TEMP(dst, thisTmp);
    }
	
	public void malloc(TEMP dst, TEMP size) {
        malloc(dst, size, true);
    }
    public void malloc(TEMP dst, TEMP size, boolean mulBy4) {
        int idxdst = dst.getSerialNumber();
        int idxsize = size.getSerialNumber();
        if (mulBy4) {
            TEMP t_4 = TEMP_FACTORY.getInstance().getFreshTEMP();
            int idxt_4 = t_4.getSerialNumber();

            append_cmd("\tli $t%d,4\n", createSet(idxt_4), emptySet(), idxt_4);
            sir_MIPS_a_lot.getInstance().mul(size, size, t_4);
        }
        append_cmd("\tmove $a0,$t%d\n" + "\tli $v0,9\n" + "\tsyscall\n",
		emptySet(), createSet(idxsize), idxsize);
				
        append_cmd("\tmove $t%d,$v0\n", createSet(idxdst), emptySet(), idxdst);
	}
	
	// move 2 temps
	public void move(TEMP dst, TEMP src) {
        int idxdst = dst.getSerialNumber();
		int idxsrc = src.getSerialNumber();
		
        append_cmd("\tmove $t%d,$t%d\n", createSet(idxdst), createSet(idxsrc), idxdst, idxsrc);
    }
	// move temp into global
    public void move(String dst, TEMP src) {
		int idxsrc = src.getSerialNumber();
		
        append_cmd(String.format("\tmove %s,$t%%d\n", dst), emptySet(), createSet(idxsrc), idxsrc);
    }
    // move global into temp
    public void move(TEMP dst, String src) {
        int idxdst = dst.getSerialNumber();
        append_cmd(String.format("\tmove $t%%d,%s\n", src), createSet(idxdst), emptySet(), idxdst);
    }
    // move 2 globals
    public void move(String dst, String src) {
        append_cmd(String.format("\tmove %s,%s\n", dst, src));
    }
	
    public void load(TEMP dst, TEMP src) {
    	load(dst, src, 0);
    }
    public void load(TEMP dst, TEMP src, int offset) {
		String cmd = String.format("\tlw $t%%d,%d($t%%d)\n", offset);
		load_word_or_byte(dst, src, cmd);
    }
    public void load_byte(TEMP dst, TEMP src) {
        String cmd = "\tlb $t%d,0($t%d)\n";
        load_word_or_byte(dst, src, cmd);
    }
    public void load_word_or_byte(TEMP dst, TEMP src, String cmd) {
        int idxdst = dst.getSerialNumber();
        int idxsrc = src.getSerialNumber();
        append_cmd(cmd, createSet(idxdst), createSet(idxsrc), idxdst, idxsrc);
    }

	// store value in src to MEM[dst+0]
    public void store(TEMP dst, TEMP src) {
        String cmd = "\tsw $t%d,0($t%d)\n";
        store_word_or_byte(dst, src, cmd);
    }
    public void store_byte(TEMP dst, TEMP src) {
        String cmd = "\tsb $t%d,0($t%d)\n";
        store_word_or_byte(dst, src, cmd);
	}
    public void store_word_or_byte(TEMP dst, TEMP src, String cmd) {
       int idxdst = dst.getSerialNumber();
       int idxsrc = src.getSerialNumber();
       append_cmd(cmd, emptySet(), createSet(idxdst, idxsrc), idxsrc, idxdst);
    }
	

	public void li(TEMP t, int value) {
		int idx = t.getSerialNumber();
		
        append_cmd(String.format("\tli $t%%d,%d\n", value), createSet(idx), emptySet(), idx);
	}
	
	public void add(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
        int dstidx = dst.getSerialNumber();

        append_cmd("\tadd $t%d,$t%d,$t%d\n", createSet(dstidx), createSet(i1, i2), dstidx, i1, i2);
    }

    public void addi(TEMP dst, TEMP var, int value) {
        int srcidx = var.getSerialNumber();
		int dstidx = dst.getSerialNumber();
		
        append_cmd(String.format("\taddi $t%%d, $t%%d, %d\n", value), createSet(dstidx), createSet(srcidx), dstidx, srcidx);
	}

    public void add_plus_plus(TEMP tmp){
        addi( tmp, tmp,1);
    }

	
	public void sub(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();
		
        append_cmd("\tsub $t%d,$t%d,$t%d\n", createSet(dstidx), createSet(i1, i2), dstidx, i1, i2);
    }

	public void mul(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();
		
        append_cmd("\tmul $t%d,$t%d,$t%d\n", createSet(dstidx), createSet(i1, i2), dstidx, i1, i2);
    }

    public void div(TEMP dst, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
		int dstidx = dst.getSerialNumber();
		
        append_cmd("\tdiv $t%d,$t%d\n", emptySet(), createSet(i1, i2), i1, i2);
        append_cmd("\tmflo $t%d\n", createSet(dstidx), emptySet(), dstidx);
	}
	
	public void slt(TEMP res, TEMP oprnd1, TEMP oprnd2) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();
		int residx = res.getSerialNumber();
		
        append_cmd("\tslt $t%d,$t%d,$t%d\n", createSet(residx), createSet(i1, i2), residx, i1, i2);
    }

	public void label(String inlabel) {
        if (labelToNodeMap.containsKey(inlabel)) {
            CFG_Node labelNode = labelToNodeMap.get(inlabel);
            addNodeToList(labelNode);
        } else { 
            CFG_Node labelNode = append_cmd(getLabelCommand(inlabel));
            labelToNodeMap.put(inlabel, labelNode);
        }
    }
    private String getLabelCommand(String label) {
        return String.format("%s:\n", label);
    }
	
	// Conditional branches
    public void bge(TEMP oprnd1, TEMP oprnd2, String label) {
        String cmd = String.format("\tbge $t%%d,$t%%d,%s\n", label);
        beq_bne_ble_blt(oprnd1,oprnd2,cmd,label);
    }
    public void bgt(TEMP oprnd1, TEMP oprnd2, String label) {
        String cmd = String.format("\tbgt $t%%d,$t%%d,%s\n", label);
        beq_bne_ble_blt(oprnd1,oprnd2,cmd,label);
    }
    public void blt(TEMP oprnd1, TEMP oprnd2, String label) {
        String cmd = String.format("\tblt $t%%d,$t%%d,%s\n", label);
        beq_bne_ble_blt(oprnd1,oprnd2,cmd,label);
    }
    public void beq(TEMP oprnd1, TEMP oprnd2, String label) {
        String cmd = String.format("\tbeq $t%%d, $t%%d,%s\n", label);
        beq_bne_ble_blt(oprnd1,oprnd2,cmd,label);
    }
    public void bne(TEMP oprnd1, TEMP oprnd2, String label) {
        String cmd = String.format("\tbne $t%%d,$t%%d,%s\n", label);
        beq_bne_ble_blt(oprnd1,oprnd2,cmd,label);
    }
    public void ble(TEMP oprnd1, TEMP oprnd2, String label) {
        String cmd = String.format("\tble $t%%d,$t%%d,%s\n", label);
        beq_bne_ble_blt(oprnd1,oprnd2,cmd,label);
    }
    public void beq_bne_ble_blt (TEMP oprnd1, TEMP oprnd2, String cmd, String label) {
        int i1 = oprnd1.getSerialNumber();
        int i2 = oprnd2.getSerialNumber();

        CFG_Node node = append_cmd(cmd, emptySet(), createSet(i1, i2), i1, i2);
        addEdgeToLabel(node, label);
    }




	public void beqz(TEMP reg, String label) {
        String cmd = String.format("\tbeq $t%%d,$zero,%s\n", label);
        bnz_beqz(reg, cmd, label);
    }
    public void bnz(TEMP reg, String label) {
		String cmd = String.format("\tbne $t%%d,$zero,%s\n", label);
        bnz_beqz(reg, cmd, label);
    }
    public void bnz_beqz(TEMP reg, String cmd, String label) {
        int regidx = reg.getSerialNumber();
        CFG_Node node = append_cmd(cmd, emptySet(), createSet(regidx), regidx);
        addEdgeToLabel(node, label);
    }

	
	// Jumps
    public void jump(String inlabel) {
        CFG_Node node = append_cmd(String.format("\tj %s\n", inlabel));
        addEdgeToLabel(node, inlabel);
        dontAddEdgeForNextCommand = true;
    }
    public void jal(String label) {
        append_cmd(String.format("\tjal %s\n", label));
    }
	public void jal(TEMP addr) {
		int t = addr.getSerialNumber();
		append_cmd("\tjal $t%d\n", emptySet(), createSet(t), t);
	}
    public void jr() {
        append_cmd("\tjr $ra\n");
        dontAddEdgeForNextCommand = true;
    }
	
	private void addEdgeToLabel(CFG_Node node, String label) {
        CFG_Node labelNode = null;
        if (!labelToNodeMap.containsKey(label)) {
            labelNode = new CFG_Node(getLabelCommand(label), emptySet(), emptySet());
            labelToNodeMap.put(label, labelNode);
        } else {
            labelNode = labelToNodeMap.get(label);
        }

        node.addEdge(labelNode);
    }

	public void runCmd(String cmd) {
        append_cmd(String.format("\t%s\n", cmd));
    }
    public void runCmd(String cmd, Integer tmp, boolean isDef) {
    	if (isDef) {
    		append_cmd(String.format("\t%s\n", cmd), createSet(tmp), emptySet(), tmp);
    	} else {
    		append_cmd(String.format("\t%s\n", cmd), emptySet(), createSet(tmp), tmp);
    	}
    }

	public void la(TEMP dst, String label) {
        int i = dst.getSerialNumber();

        append_cmd(String.format("\tla $t%%d, %s\n", label), createSet(i), emptySet(), i);
    }


    public void loadLWFpAddressWithOffset(TEMP dst, int offset) {
        String cmd = String.format("\tlw $t%%d,%d($fp)\n", offset);
        load_Fp_Address(dst, cmd);
    }
    public void loadFpAddressWithOffset(TEMP dst, int offset) {
        String cmd = String.format("\taddi $t%%d,$fp,%d\n", offset);
        load_Fp_Address(dst, cmd);
    }
    public void load_Fp_Address(TEMP dst, String cmd) {
        int tempNum = dst.getSerialNumber();
        append_cmd(cmd, createSet(tempNum), emptySet(), tempNum);
    }

    public static Set<Integer> createSet(Integer... nums) {
        HashSet<Integer> integers = new HashSet<>();
        for (Integer num : nums) 
        {
            integers.add(num);
        }
        return integers;
    }

    public void printCommandsToFile(Map<Integer, Integer> regMap) 
    {
        for (CFG_Node node : commandList) 
        {
            node.printToFile(fileWriter, regMap);
        }
    }

    public void storeToFpAddressWithOffset(TEMP dst, int offset) {
        int tempNum = dst.getSerialNumber();
        append_cmd(String.format("\tsw $t%%d,%d($fp)\n", offset), emptySet(), createSet(tempNum), tempNum);
    }
	public void checkPointerDereference(TEMP thisPointer) {
        String continueLabel = IRcommand.getFreshLabel("Not_Nil");
        sir_MIPS_a_lot.getInstance().bnz(thisPointer, continueLabel);

        sir_MIPS_a_lot.getInstance().print_string_by_label(sir_MIPS_a_lot.POINTER_DEREFERENCE);
        sir_MIPS_a_lot.getInstance().exitProgram();

        sir_MIPS_a_lot.getInstance().label(continueLabel);
    }

	private CFG_Node append_cmd(String command) {
	    return append_cmd(command, emptySet(), emptySet());
	}

	private CFG_Node append_cmd(String command, Set<Integer> def, Set<Integer> use, Integer... tempVars) {
	    CFG_Node node = new CFG_Node(command, def, use, tempVars);
	    addNodeToList(node);
	    return node;
	}

	// Adds the command to the list of commands
	private void addNodeToList(CFG_Node node) {
	    node.commandIndex = commandList.size();
	    commandList.add(node);
	    if (!dontAddEdgeForNextCommand && commandList.size() > 1) {
	        commandList.get(commandList.size() - 2).addEdge(node);
	    }
	    dontAddEdgeForNextCommand = false;
	}

	public static void addGlobalVariable(String varName) {
	    globalVars.add(varName);
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static sir_MIPS_a_lot instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected sir_MIPS_a_lot() {
        commandList = new ArrayList<>();
        labelToNodeMap = new HashMap<>();
    }

    public List<CFG_Node> getCommandList() {
        return commandList;
    }

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static sir_MIPS_a_lot getInstance() {
        if (instance == null) {
            /*******************************/
			/* [0] The instance itself ... */
            /*******************************/
            instance = new sir_MIPS_a_lot();
        }
        return instance;
    }

    public void start_writing (String outputFilename)
    {
        try 
        {
            instance.fileWriter = new PrintWriter(outputFilename);
            instance.fileCreated = true;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        instance.fileWriter.print(".data\n");
        instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Division By Zero\"\n");
        instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
        instance.fileWriter.print(POINTER_DEREFERENCE + ": .asciiz \"Invalid Pointer Dereference\"\n");
	    instance.fileWriter.format(funcNames);


        for (String var : globalVars)
        {
            instance.fileWriter.printf("%s: .word 0\n", var);
        }
        int numOfStrings = AST_EXP_STRING.allStrings().size();
        for (int i = 0; i < numOfStrings; i++) {
            String label = AST_EXP_STRING.getStringLabel(i);
            String str = AST_EXP_STRING.allStrings().get(i);
            instance.fileWriter.printf("%s: .asciiz \"%s\"\n", label,str);
        }

        for (String key : vtables.keySet())
        {
            if (!vtables.get(key).isEmpty()){
                String vString = String.join(", ", vtables.get(key));
                instance.fileWriter.printf("%s: .word %s\n", key, vString);
            }

        }

        for (String label : classInitValues.keySet()) {
            if (classInitValues.get(label).isEmpty()) {
                continue;
            }
            String valStr = String.join(", ", classInitValues.get(label));
            instance.fileWriter.printf("%s: .word %s\n", label, valStr);
        }

        instance.fileWriter.print(".text\n");
	}

}
