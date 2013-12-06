/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author raf
 * iterator cez vsetky mozne k-grafy, kde k je pocet znakov abecedy
 */
public class TransitionsIterator implements Iterator {

    HashMap<Character,MatrixIterator> TransitionMatrixIterators;
    HashMap<Character,Matrix> currentMatrices;
    int numberOfStates;
    long limit, state;
    
    
    public TransitionsIterator(int n){
        this.TransitionMatrixIterators = new HashMap<>();
        this.currentMatrices = new HashMap<>();
        
        for(Character c :  Variables.alphabet){
            MatrixIterator it = new MatrixIterator(n);
            TransitionMatrixIterators.put(c, it);
            this.currentMatrices.put(c, it.next());
        }
        
        
        this.numberOfStates = n;
        this.limit = (long)Math.pow(2, 2*n * n);
        this.state = 0;
    }
    
    @Override
    public boolean hasNext() {
        return (this.state < limit);
    }

    @Override
    public HashMap<Character, Matrix> next() {
        
        HashMap<Character, Matrix> ret = new HashMap<>();
        for(Character c : Variables.alphabet){
            ret.put(c, this.currentMatrices.get(c));
        }
        
        for(Character c : Variables.alphabet){
            MatrixIterator it = this.TransitionMatrixIterators.get(c);
            if (!it.hasNext()){
                it = new MatrixIterator(this.numberOfStates);
                Matrix m = it.next();
                this.TransitionMatrixIterators.put(c,it);
                this.currentMatrices.put(c, m);
            }
            else{
                this.currentMatrices.put(c, it.next());
                break;
            }
        }
        
        this.state++;
        return ret;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
