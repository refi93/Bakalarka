/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author raf
 */

/* trieda, ktorej ulohou je vratit vsetky podmnoziny nejakeho ArrayListu bez optimalizacii */
public class NaiveSubsetIterator extends SubsetIterator{
    
    
    public NaiveSubsetIterator(int n){
        super(n);
        this.state = 0;
        this.limit = (long) Math.pow(2, n); // upravime limit, chceme ist skutocne cez vsetky podmnoziny
    }
    
    @Override
    public void reset(){
        super.reset();
        this.state = 0;
        this.limit = (long) Math.pow(2, n); // upravime limit, chceme ist skutocne cez vsetky podmnoziny
    }
    
    @Override
    public HashSet<Identificator> next(){
        if (!this.hasNext()) return null;
        HashSet<Identificator> ret = new HashSet<>();
        long pom = state;
        
        int i = 0;
        while(pom > 0){
            
            if (pom % 2 == 1){
                ret.add(this.allElements.get(i));
            }
            i++;
            pom /= 2;
        }
        
        this.state++;
        return ret;
        
    }
 
}
