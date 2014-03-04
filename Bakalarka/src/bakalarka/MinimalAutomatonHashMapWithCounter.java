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
 * funguje podobne ako MinimalAutomatonHashMap, ale pamatame si ku kazdemu jazyku aj
 * pocet NKA, ktory ho akceptuje, ktore sme sa pokusili vlozit
 */
public class MinimalAutomatonHashMapWithCounter extends MinimalAutomatonHashMap {
    HashMap<Tuple,Integer> allMinDFACodesWithCounter;
    private long size = 0;
    
    public MinimalAutomatonHashMapWithCounter(){
        this.allMinDFACodesWithCounter = new HashMap<>();
    }
    
    
    
    
    /* overi, ci sa tu nenachadza uz nejaky iny ekvivalentny automat */
    public boolean containsEquivalent(Automaton a) throws Exception{
        Tuple hash = a.myHashCode();
        return this.allMinDFACodesWithCounter.containsKey(hash);
    }
    
    
    /* vrati true, ak je pridany automat novy */
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean ret;
        
        // ak sa kod jazyka este nenachadza v mape, vlozime k tomu kodu jazyka pocitadlo nastavene na 0
        if (!this.allMinDFACodesWithCounter.containsKey(a.myHashCode())){
            ret = true;
            this.allMinDFACodesWithCounter.put(a.myHashCode(), 1);
        }
        else{ // inak zvacsime pocitadlo
            int x = this.allMinDFACodesWithCounter.get(a.myHashCode());
            x++;
            this.allMinDFACodesWithCounter.put(a.myHashCode(), x);
            ret = false;
        }
        if (ret) this.size++;
        return ret;
    }
    
    /* vlozenie rovno hodnoty bez kontroly - pouziva sa pri mergeovani threadov */
    public void insertValue(Tuple hash){
        if(!this.allMinDFACodesWithCounter.containsKey(hash)){
            this.size++;
        }
        else{
            this.allMinDFACodesWithCounter.put(hash, 1);
        }
    }
    
    
    public void clear(){
        this.size = 0;
        this.max_collisions = 0;
        this.comparison_count = 0;
        this.allMinDFACodesWithCounter.clear();
    }
}
