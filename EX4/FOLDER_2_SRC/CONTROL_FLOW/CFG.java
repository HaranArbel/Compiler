package CONTROL_FLOW;

import TEMP.TEMP_FACTORY;
import MIPS.sir_MIPS_a_lot;
import java.util.*;

public class CFG {

    public static final int NUM_REG = 8;
    
    public static void colorMe(List<CFG_Node> list_of_commands) {
        List<Set<Integer>> interference_graph = init_Interference_Graph(list_of_commands);
        sir_MIPS_a_lot.getInstance().printCommandsToFile(init_Valid_Coloring(interference_graph));
    }

    // return Map. // temp(s) -> register
    static Map<Integer, Integer> init_Valid_Coloring(List<Set<Integer>> graph) {

        int graphSize = graph.size();
        // sort nodes by degree
        Integer[] perm = new Integer[graphSize];
        for (int i = 0; i < graphSize ; i++)
            perm[i] = i; // initiation
        Arrays.sort(perm, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return graph.get(o2).size() - graph.get(o1).size(); // highest to lowest degree
            }
        }); 

        Map<Integer, Integer> colored = new HashMap<>();
        // color nodes, sorted by degree
        for (int i : perm) {
            boolean[] notAvailableColors = {false, false, false, false, false , false, false, false};
            Set<Integer> neighbors = graph.get(i);


            // get unavailable colors
            for (int neigh : neighbors) {
                // check neigbors has a color
                if (colored.containsKey(neigh)) notAvailableColors[colored.get(neigh)] = true;
            }
            // find minimal available color
            int reg_Bound = NUM_REG-1;
            for (int j = 0; j <= reg_Bound ; j++){
                if (notAvailableColors[j] == false){
                    colored.put(i, j);
                    break;
                }
                if (j == reg_Bound) throw new RuntimeException("Ohh No ! We CAN'T COLOR! we found a node with at 8 or degree");
            }
        }
        return colored;
    }



    public static List<Set<Integer>> init_Interference_Graph(List<CFG_Node> commands_list) {
        boolean finish = false;
        while (!finish){
            for (CFG_Node cmd : commands_list){
                cmd.updateLiveIn();
                cmd.updateLiveOut();
            }

            finish = true;
            for (CFG_Node cmd : commands_list) 
                if ((!cmd.liveIn.equals(cmd.liveInTag)) || (!cmd.liveOut.equals(cmd.liveOutTag))) 
                    finish = false;
            for (CFG_Node cmd : commands_list) {
                cmd.liveIn.clear();
                cmd.liveIn.addAll(cmd.liveInTag);
                cmd.liveOut.clear();
                cmd.liveOut.addAll(cmd.liveOutTag);
            }
        }

        int size = TEMP_FACTORY.getInstance().getFreshTEMP().getSerialNumber();
        List<Set<Integer>> result = new ArrayList<>(size); // list of neighbors for each var
        for (int i = 0 ; i < size ; i++)
            result.add(new HashSet<>());

        for (CFG_Node cmd : commands_list) 
            if (cmd.liveOut.size() > 1) 
                init_Clique(cmd.liveOut, result);
        return result;
    }

    static void init_Clique (Set<Integer> clique, List<Set<Integer>> graph){
        if (clique.size() <= NUM_REG){
            for (Integer i : clique){
                graph.get(i).addAll(clique);
                graph.get(i).remove(i);
            }
        } else throw new RuntimeException("CAN'T COLOR! (there is a a node with a degree > 8");
    }
}