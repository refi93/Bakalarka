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
import java.util.HashSet;

/**
 *
 * @author raf
 * tato trieda ma za ciel pamatat si vsetky dosial najdene minimalne NFA
 * a dokazeme do nej aj vlozit nejake NFA a ona rozhodne, ci uz tam neni
 */
public class MinimalAutomatonHashMap {
    HashMap<Triplet, Tuple<Integer, Integer> > AutomatonClasses;
    ArrayList<Triplet> allMinDFACodes;
    public int comparison_count = 0;
    int max_collisions = 0;
    private long size = 0;
    
    public MinimalAutomatonHashMap(){
        this.AutomatonClasses = new HashMap<>();
        this.allMinDFACodes = new ArrayList<>();
    }
    
    
    /* out - odkaz kam vypisovat, startIndex - pociatocny index, od ktoreho vypisovat automaty */
    public void print(FastPrint out, int startIndex) throws IOException, Exception{
        out.println(Integer.valueOf(this.allMinDFACodes.size()).toString());
        for(int i = startIndex; i < this.allMinDFACodes.size();i++){
            out.println(AutomatonClasses.get(allMinDFACodes.get(i)).toString());
        }
    }
    
    
    /* overi, ci sa tu nenachadza uz nejaky iny ekvivalentny automat */
    public boolean containsEquivalent(Automaton a) throws Exception{
        boolean ret = false;
        Triplet hash = a.myHashCode();
        if (AutomatonClasses.containsKey(hash)) {
            return true;
        }
        return ret;
    }
    
    
    /* ked sme si isty, ze dany automat je minimalny, tak ho mozme vlozit nasilu
    pomocou tejto metody
    */
    private void forceInsert(Automaton a) throws Exception {
        Triplet hash = a.myHashCode();
        this.size++;
        AutomatonClasses.put(hash,new Tuple<Integer,Integer>(a.getNumberOfStates(),a.minimalDFA().getNumberOfStates()));
        allMinDFACodes.add(hash);
    }
    
    
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean isNew = !this.containsEquivalent(a);
        if (isNew){
            this.forceInsert(a);
        }
        return isNew;
    }
    
    
    public void tryToInsertValue(Triplet hash,Tuple<Integer,Integer> NFADFAcomparison){
        if(!this.AutomatonClasses.containsKey(hash)){
            this.size++;
            this.allMinDFACodes.add(hash);
        }
        this.AutomatonClasses.put(hash, NFADFAcomparison);
    }
    
    
    public void clear(){
        this.AutomatonClasses.clear();
        this.size = 0;
        this.max_collisions = 0;
        this.comparison_count = 0;
    }
    
    public long size(){
        return this.size;
    }
}
