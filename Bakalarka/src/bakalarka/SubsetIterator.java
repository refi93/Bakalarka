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

/* trieda, ktorej ulohou je vratit podmnoziny ArrayListu - ciel je vygenerovat 
mnoziny konecnych stavov s optimalizaciami */
public class SubsetIterator implements Iterator{
    ArrayList<Identificator> allElements;
    long state;
    long limit;
    int n;
    
    
    public SubsetIterator(int n){
        this.n = n;
        this.allElements = new ArrayList<>();
        for(int i = 0;i < n;i++){
            this.allElements.add(new Identificator(i));
        }
        state = 1; // od 1 zaciname, lebo ignorujeme prazdnu mnozinu akceptacnych stavov
        if (n == 1){
            state = 0; // ak ale mame len 1-stavove, automaty, tam je dolezita aj prazdna mnozina, lebo prazdny jazyk
        }
        //this.limit = (long)Math.pow(2,allElements.size());
        this.limit = 2 * n;
    }
    
   
    // resetnutie subsetIteratora do pociatocneho stavu
    public void reset(){
        state = 1; // od 1 zaciname, lebo ignorujeme prazdnu mnozinu akceptacnych stavov
        if (n == 1){
            state = 0; // ak ale mame len 1-stavove, automaty, tam je dolezita aj prazdna mnozina, lebo prazdny jazyk
        }
        //this.limit = (long)Math.pow(2,allElements.size());
        this.limit = 2 * n;
    }
    
    
    public void skip(){
        if (Variables.alphabet.size() == 2){
            this.next();
            return;
        }
        if (!this.hasNext()) return;
        this.state++;
    }
    
    @Override
    public HashSet<Identificator> next(){
        if (!this.hasNext()) return null;
        HashSet<Identificator> ret = new HashSet<>();
        long pom = state;
        
        if (pom % 2 == 1){
            ret.add(this.allElements.get(0));
        }
        
        pom /= 2;
        
        for(int i = 1;i <= pom;i++){
            ret.add(this.allElements.get(i));
        }
        this.state++;
        return ret;
        
    }
    
    
    @Override
    public boolean hasNext(){
        return (state < limit);
    }

    /* vrati nahodnu podmnozinu */
    public HashSet<Identificator> random(){
        long pom = this.state;
        // zaujimaju nas vsetky mozne mnoziny okrem prazdnej
        this.state = Math.abs(Variables.generator.nextLong()) % (this.limit - 1) + 1;
        HashSet<Identificator> ret = this.next();
        this.state = pom;
        return ret;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
