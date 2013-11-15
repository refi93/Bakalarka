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

    public TupleIdentificator(Identificator a, Identificator b){
        this.clear();
        this.add(a);
        this.add(b);
    }
    
    public TupleIdentificator(){
        super();
    }
    
    @Override
    public TupleIdentificator copy() {
        TupleIdentificator ret = new TupleIdentificator();
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
