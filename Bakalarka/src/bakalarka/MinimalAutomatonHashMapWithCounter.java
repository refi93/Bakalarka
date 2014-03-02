/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author raf
 */
public class MinimalAutomatonHashMapWithCounter extends MinimalAutomatonHashMap {
    HashMap<Tuple,Integer> allMinDFACodes;
    public int comparison_count = 0;
    int max_collisions = 0;
    private long size = 0;
    
    public MinimalAutomatonHashMapWithCounter(){
        this.allMinDFACodes = new HashMap<>();
    }
    
    
    
    
    /* overi, ci sa tu nenachadza uz nejaky iny ekvivalentny automat */
    public boolean containsEquivalent(Automaton a) throws Exception{
        Tuple hash = a.myHashCode();
        return this.allMinDFACodes.containsKey(hash);
    }
    
    
    /* vrati true, ak je pridany automat novy */
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean ret;
        
        // ak sa kod jazyka este nenachadza v mape, vlozime k tomu kodu jazyka pocitadlo nastavene na 0
        if (!this.allMinDFACodes.containsKey(a.myHashCode())){
            ret = true;
            this.allMinDFACodes.put(a.myHashCode(), 0);
        }
        else{ // inak zvacsime pocitadlo
            int x = this.allMinDFACodes.get(a.myHashCode());
            x++;
            this.allMinDFACodes.put(a.myHashCode(), x);
            ret = false;
        }
        if (ret) this.size++;
        return ret;
    }
    
    /* vlozenie rovno hodnoty bez kontroly - pouziva sa pri mergeovani threadov */
    public void insertValue(Tuple hash){
        if(!this.allMinDFACodes.containsKey(hash)){
            this.size++;
        }
        else{
            this.allMinDFACodes.put(hash, 0);
        }
    }
    
    
    public void clear(){
        this.size = 0;
        this.max_collisions = 0;
        this.comparison_count = 0;
        this.allMinDFACodes.clear();
    }
}
