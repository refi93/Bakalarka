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
public class PowersetIdentificator extends HashSet<Identificator> implements Identificator {

    
    @Override
    public PowersetIdentificator copy() {
        PowersetIdentificator ret = new PowersetIdentificator();
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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PowersetIdentificator other = (PowersetIdentificator) obj;
        if (this.hashCode() != other.hashCode()) {
            return false;
        }
        return true;
    }
    
}
