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
 * @author raf iterator cez vsetky mozne k-grafy, kde k je pocet znakov abecedy
 * teda je to iterator cez vsetky mozne delta-funkcie pre dany pocet stavov
 */
public class TransitionsIterator implements Iterator {

    HashMap<Character, MatrixIterator> TransitionMatrixIterators;
    HashMap<Character, Matrix> currentMatrices;
    int numberOfStates;
    long limit, state;

    public TransitionsIterator(int n) {
        this.TransitionMatrixIterators = new HashMap<>();
        this.currentMatrices = new HashMap<>();

        for (Character c : Variables.alphabet) {
            MatrixIterator it = new MatrixIterator(n);
            TransitionMatrixIterators.put(c, it);
            this.currentMatrices.put(c, it.next());
        }

        this.numberOfStates = n;
        this.limit = (long) Math.pow(2, 2 * n * n);
        this.state = 0;
    }

    
    public void skip() {
        if (Variables.alphabet.size() == 2){
            this.next();
            return;
        }
        for (Character c : Variables.alphabet) {
            MatrixIterator it = this.TransitionMatrixIterators.get(c);
            if (!it.hasNext()) {
                it = new MatrixIterator(this.numberOfStates);

                // toto funguje len na 2-pismenkovej abecede - prehodenie 1 a 0 v delta-funckii
                if (c.equals(Variables.alphabet.get(0)) && (Variables.alphabet.size() == 2)) {
                    it = new MatrixIterator(this.numberOfStates, this.TransitionMatrixIterators.get(Variables.alphabet.get(1)));
                }
                // ---------------------------

                it.skip();
                this.TransitionMatrixIterators.put(c, it);
            } else {
                it.skip();
                break;
            }
        }

        this.state++;
    }
    
    @Override
    public boolean hasNext() {
        for (Character c : Variables.alphabet) {
            if (this.TransitionMatrixIterators.get(c).hasNext()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<Character, Matrix> next() {

        HashMap<Character, Matrix> ret = new HashMap<>();
        for (Character c : Variables.alphabet) {
            ret.put(c, this.currentMatrices.get(c));
        }

        for (Character c : Variables.alphabet) {
            MatrixIterator it = this.TransitionMatrixIterators.get(c);
            if (!it.hasNext()) {
                it = new MatrixIterator(this.numberOfStates);

                // toto funguje len na 2-pismenkovej abecede - prehodenie 1 a 0 v delta-funckii

                if ((c.equals(Variables.alphabet.get(0))) && (Variables.alphabet.size() == 2)) {
                    it = new MatrixIterator(this.numberOfStates, this.TransitionMatrixIterators.get(Variables.alphabet.get(1)));
                }
                
                Matrix m = it.next();
                
                // --------------------------
                this.TransitionMatrixIterators.put(c, it);
                this.currentMatrices.put(c, m);
            } else {
                Matrix m = it.next();
                
                // optimalizacia pre 2-pismenkovu abecedu - zahadzujeme nesuvisle delta-funkcie
                if ((Variables.alphabet.size() == 2) && (c.equals('0'))){
                    while(it.hasNext() && (!m.union(this.currentMatrices.get('1')).isConnected())){
                        m = it.next();
                    }
                }
                // koniec optimalizacie
                
                this.currentMatrices.put(c, m);
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
