/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.Iterator;

/**
 *
 * @author raf
 * Iterator iterujuci cez stavy od 0 po n - 1
 */
public class IntegerIterator implements Iterator {

    int value = 0;
    int range;
    
    public IntegerIterator(int range){
        this.value = 0;
        this.range = range;
    }
    
    @Override
    public boolean hasNext() {
        return (this.value < this.range);
    }

    public void skip(){
        if (Variables.alphabet.size() == 2){
            this.next();
            return;
        }
        this.value++;
    }
    
    @Override
    public Identificator next() {
        int ret = this.value;
        this.value++;
        return new Identificator(ret);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
