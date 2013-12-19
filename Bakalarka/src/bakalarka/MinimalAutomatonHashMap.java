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
    HashSet<Triplet> allMinDFACodes;
    public int comparison_count = 0;
    int max_collisions = 0;
    private long size = 0;
    
    public MinimalAutomatonHashMap(){
        this.allMinDFACodes = new HashSet<>();
    }
    
    
    
    
    /* overi, ci sa tu nenachadza uz nejaky iny ekvivalentny automat */
    public boolean containsEquivalent(Automaton a) throws Exception{
        Triplet hash = a.myHashCode();
        return this.allMinDFACodes.contains(hash);
    }
    
    
    
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean ret = this.allMinDFACodes.add(a.myHashCode());
        if (ret) this.size++;
        return ret;
    }
    
    
    public void tryToInsertValue(Triplet hash){
        if(this.allMinDFACodes.add(hash)){
            this.size++;
        }
    }
    
    
    public void clear(){
        this.size = 0;
        this.max_collisions = 0;
        this.comparison_count = 0;
    }
    
    public long size(){
        return this.size;
    }
}
