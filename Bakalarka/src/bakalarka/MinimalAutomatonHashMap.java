/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author raf
 * tato trieda ma za ciel pamatat si vsetky dosial najdene minimalne NFA
 * a dokazeme do nej aj vlozit nejake NFA a ona rozhodne, ci uz tam neni
 */
public class MinimalAutomatonHashMap {
    HashMap<Long,ArrayList<Automaton> > AutomatonClasses = new HashMap<>();
    ArrayList<Automaton> allMinNFAs;
    //int max = 0;
    
    public MinimalAutomatonHashMap(){
        this.AutomatonClasses = new HashMap<>();
        this.allMinNFAs = new ArrayList<>();
    }
    
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean isNew = true;
        long hash = a.myHashCode();
        if (AutomatonClasses.get(hash) != null) {
            for (Automaton previous : AutomatonClasses.get(hash)) {
                if (previous.equivalent(a)) {
                    isNew = false;
                    break;
                }
                /*
                na ratanie "bezpecnej dlzky slov" - dlzky, pri ktorej uz 2 automaty nebudu kolidovat
                else{
                    int i = 4;
                    while(true){
                        if (i > max) max = i;
                        if (!previous.allWordsOfLength(i).equals(a.allWordsOfLength(i))){
                            //System.out.println("COLLISION");
                            //System.out.println(a.toString() + previous.toString());
                            break;
                        }
                        i++;
                    }
                }*/
            }
        }
        if (isNew) {
            allMinNFAs.add(a);
            if (AutomatonClasses.get(hash) != null) {
                AutomatonClasses.get(hash).add(a);
            } else {
                AutomatonClasses.put(hash, new ArrayList<Automaton>());
                AutomatonClasses.get(hash).add(a);
            }
        }
        return isNew;
    }
}
