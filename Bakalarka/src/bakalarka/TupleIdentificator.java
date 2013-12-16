/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;

/**
 *
 * @author raf
 */

/* Identifikator vyjadrujuci usporiadanu n-ticu */
public class TupleIdentificator extends ArrayList<Identificator> implements Identificator {

    
    int hash_cache; // cachovany hash
    
    public TupleIdentificator(Identificator a, Identificator b){
        this.hash_cache = -1;
        this.clear();
        this.add(a);
        this.add(b);
    }
    
    public TupleIdentificator(){
        super();
        this.hash_cache = -1;
    }
    
    @Override
    public TupleIdentificator copy() {
        TupleIdentificator ret = new TupleIdentificator();
        for(Identificator x : this){
            ret.add(x.copy());
        }
        return ret;
    }
    
    
    @Override
    public int hashCode(){
        if (this.hash_cache != -1){
            return this.hash_cache;
        }
        else{
            this.hash_cache = this.get(0).hashCode() ^ this.get(1).hashCode();
            return this.hash_cache;
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
        final TupleIdentificator other = (TupleIdentificator) obj;
        if (this.hashCode()!=other.hashCode()) return false;
        if (!this.get(0).equals(((TupleIdentificator)obj).get(0))) return false;
        if (!this.get(1).equals(((TupleIdentificator)obj).get(1))) return false;
        return true;
    }
    
    @Override
    public boolean add(Identificator id){
        if(this.size() > 1) return false; // umoznujeme vlozit len 2 prvky
        else{
            return super.add(id);
        }
    }
    
}
