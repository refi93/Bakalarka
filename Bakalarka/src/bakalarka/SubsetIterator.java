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

/* trieda, ktorej ulohou je vratit vsetky podmnoziny nejakeho ArrayListu */
public class SubsetIterator implements Iterator{
    ArrayList<Identificator> allElements;
    long state;
    
    public SubsetIterator(int n){
        this.allElements = new ArrayList<>();
        for(int i = 0;i < n;i++){
            this.allElements.add(new IntegerIdentificator(i));
        }
        state = 0;
    }

    
    public SubsetIterator(ArrayList<Identificator> allElements){
        this.allElements = new ArrayList<>();
        for(Identificator id : allElements){
            this.allElements.add(id);
        }
        state = 0;
    }
    
    
    @Override
    public HashSet<Identificator> next(){
        if (!this.hasNext()) return null;
        HashSet<Identificator> ret = new HashSet<>();
        long pom = state;
        for(int i = 0;i < allElements.size();i++){
            if (pom % 2 == 1){
                ret.add(this.allElements.get(i));
            }
            pom /= 2;
        }
        this.state++;
        return ret;
        
    }
    
    
    @Override
    public boolean hasNext(){
        return (state < Math.pow(2,allElements.size()));
    }

    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
