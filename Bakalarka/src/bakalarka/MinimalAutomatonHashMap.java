/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author raf
 * tato trieda ma za ciel pamatat si vsetky dosial najdene minimalne NFA
 * a dokazeme do nej aj vlozit nejake NFA a ona rozhodne, ci uz tam neni
 */
public class MinimalAutomatonHashMap {
    HashMap<BigIntegerTuple,ArrayList<Automaton> > AutomatonClasses = new HashMap<>();
    ArrayList<Automaton> allMinNFAs;
    public int comparison_count = 0;
    int max_collisions = 0;
    private long size = 0;
    
    public MinimalAutomatonHashMap(){
        this.AutomatonClasses = new HashMap<>();
        this.allMinNFAs = new ArrayList<>();
    }
    
    
    /* out - odkaz kam vypisovat, startIndex - pociatocny index, od ktoreho vypisovat automaty */
    public void print(FastPrint out, int startIndex) throws IOException, Exception{
        out.println(Integer.valueOf(this.allMinNFAs.size()).toString());
        for(int i = startIndex; i < this.allMinNFAs.size();i++){
            Experiments.printAutomaton(allMinNFAs.get(i), i, out,false); // vypiseme a nechceme minimalne DFA
        }
    }
    
    
    /* overi, ci sa tu nenachadza uz nejaky iny ekvivalentny automat */
    public boolean containsEquivalent(Automaton a) throws Exception{
        boolean ret = false;
        BigIntegerTuple hash = a.myHashCode();
        if (AutomatonClasses.get(hash) != null) {
            /*for (Automaton previous : AutomatonClasses.get(hash)) {
                comparison_count++;
                if (previous.equivalent(a)) {
                    ret = true;
                    break;
                }
            }*/
            return true;
        }
        return ret;
    }
    
    
    /* ked sme si isty, ze dany automat je minimalny, tak ho mozme vlozit nasilu
    pomocou tejto metody
    */
    public void forceInsert(Automaton a) throws Exception {
        BigIntegerTuple hash = a.myHashCode();
        allMinNFAs.add(a);
        this.size++;
        if (AutomatonClasses.get(hash) != null) {
            AutomatonClasses.get(hash).add(a);
        } else {
            AutomatonClasses.put(hash, new ArrayList<Automaton>());
            AutomatonClasses.get(hash).add(a);
        }

        /*if (AutomatonClasses.get(hash).size() > max_collisions){
         max_collisions = AutomatonClasses.get(hash).size();
         System.out.println(max_collisions);
         }*/
    }
    
    
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean isNew = !this.containsEquivalent(a);
        if (isNew){
            this.forceInsert(a);
        }
        return isNew;
    }
    
    
    public void clear(){
        this.AutomatonClasses.clear();
        this.allMinNFAs.clear();
        this.size = 0;
        this.max_collisions = 0;
        this.comparison_count = 0;
    }
    
    public long size(){
        return this.size;
    }
}
