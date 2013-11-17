/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author raf
 */

/* trieda, ktorej ulohou je vratit vsetky podmnoziny nejakeho ArrayListu */
public class AllSubsets {
    ArrayList<Identificator> allElements;
    long state;
    
    
    public AllSubsets(ArrayList<Identificator> allElements){
        this.allElements = new ArrayList<>();
        for(Identificator id : allElements){
            this.allElements.add(id);
        }
        state = 0;
    }
    
    
    public HashSet<Identificator> next(){
        HashSet<Identificator> ret = new HashSet<>();
        long pom = state;
        for(int i = 0;i < allElements.size();i++){
            if (pom % 2 == 1){
                ret.add(this.allElements.get(i));
            }
            pom /= 2;
        }
        state++;
        return ret;
    }
    
    
    public boolean hasNext(){
        return (state < Math.pow(2,allElements.size()));
    }
}
