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
    HashMap<Integer,ArrayList<Automaton> > AutomatonClasses = new HashMap<>();
    ArrayList<Automaton> allMinNFAs;
    
    public MinimalAutomatonHashMap(){
        this.AutomatonClasses = new HashMap<>();
        this.allMinNFAs = new ArrayList<>();
    }
    
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean isNew = true;
        int hash = a.hashCode();
        if (AutomatonClasses.get(hash) != null) {
            for (Automaton previous : AutomatonClasses.get(hash)) {
                if (previous.equivalent(a)) {
                    isNew = false;
                    break;
                }
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
