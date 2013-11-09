/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.HashSet;

/**
 *
 * @author raf
 * identifikator stavu dany potencnou mnozinou
 */
public class PowerSetIdentificator extends HashSet<Identificator> implements Identificator {

    
    @Override
    public PowerSetIdentificator copy() {
        PowerSetIdentificator ret = new PowerSetIdentificator();
        for(Identificator x:this){
            ret.add(x.copy());
        }
        return ret;
    }
    
    
    @Override
    public int hashCode(){
        return super.hashCode();
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        return super.equals(obj);
    }
    
}
