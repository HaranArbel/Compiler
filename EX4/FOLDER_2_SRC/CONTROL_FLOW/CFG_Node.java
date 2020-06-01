package CONTROL_FLOW;

import MIPS.*;

import java.io.PrintWriter;
import java.util.*;
import java.util.HashSet;

public class CFG_Node {

    public int commandIndex;
    public String command;
    public Set<CFG_Node> edgesOut;
    public Set<Integer> liveOut, liveIn, liveOutTag, liveInTag;
    public Set<Integer> def;
    public Set<Integer> use;
    private final Integer[] tempVarsForFormat;

    public CFG_Node(String command, Set<Integer> def, Set<Integer> use, Integer... tempVarsForFormat) {
        this.command = command;
        this.def = def;
        this.use = use;
        this.tempVarsForFormat = tempVarsForFormat;

        edgesOut = new HashSet<>();
        liveOut = new HashSet<>();
        liveIn = new HashSet<>();
        liveOutTag = new HashSet<>();
        liveInTag = new HashSet<>();
    }

    public void addEdge(CFG_Node node){
        edgesOut.add(node);
    }

    // build the liveness range
    public void updateLiveIn() {
        this.liveInTag.clear();
        this.liveInTag.addAll(this.liveOut);
        this.liveInTag.removeAll(this.def);
        this.liveInTag.addAll(this.use);
    }
    public void updateLiveOut() {
        this.liveOutTag.clear();
        for (CFG_Node succ : this.edgesOut){
            this.liveOutTag.addAll(succ.liveInTag);
        }
    }

    public void printToFile(PrintWriter fileWriter, Map<Integer, Integer> regMap) {
        // DCE, this is a definition of a dead value
        if (!this.def.isEmpty() && !this.liveOut.containsAll(this.def)){
            return; 
        }
        Integer[] registerFormat = new Integer[tempVarsForFormat.length];
        for (int i = 0; i < registerFormat.length; i++) {
            registerFormat[i] = regMap.get(tempVarsForFormat[i]);
        }
        
        fileWriter.printf(command, (Object[])registerFormat);
    }
}
