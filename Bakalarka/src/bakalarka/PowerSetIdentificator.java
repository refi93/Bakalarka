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

    int hash_cache = -1;
    
    public PowerSetIdentificator(HashSet<Identificator> statesSet){
        this.clear();
        for (Identificator id : statesSet){
            this.add(id);
        }
    }
    
    public PowerSetIdentificator(){
        super();
    }
    
    @Override
    public PowerSetIdentificator copy() {
        PowerSetIdentificator ret = new PowerSetIdentificator();
        for(Identificator x:this){
            ret.add(x.copy());
        }
        ret.hash_cache = this.hash_cache;
        return ret;
    }

    @Override
    public boolean add(Identificator x){
        this.hash_cache = -1; // resetneme cachenutu hash_value
        return super.add(x);
    }
    
    @Override
    public int hashCode(){
        if (hash_cache != -1){
            return hash_cache;
        }
        else{
            hash_cache = super.hashCode();
            return hash_cache;
        }
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PowerSetIdentificator other = (PowerSetIdentificator) obj;
        if (other.hashCode() != this.hashCode()) return false;
        return super.equals(other);
    }
    
}
