/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.HashMap;

/**
 *
 * @author raf
 */
public class NaiveTransitionsIterator extends TransitionsIterator {

    public NaiveTransitionsIterator(int n) {
        super(n);
    }
    
    
    @Override
    public HashMap<Character, Matrix> next() {
        if (!this.hasNext()) return null;
        super.checkNext();
        
        HashMap<Character, Matrix> ret = new HashMap<>();
        for (Character c : Variables.alphabet) {
            ret.put(c, this.currentMatrices.get(c));
        }
        
        for (Character c : Variables.alphabet) {
            MatrixIterator it = this.TransitionMatrixIterators.get(c);
            if (!it.hasNext()) {
                it = new MatrixIterator(this.numberOfStates);
                
                Matrix m = it.next();
                
                this.TransitionMatrixIterators.put(c, it);
                this.currentMatrices.put(c, m);
            } else {
                Matrix m = it.next();
                
                this.currentMatrices.put(c, m);
                break;
            }
        }

        this.state++;
        return ret;
    }
    
}
