/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalarka;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

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

                /* pozor optimalizacia specialne pre 2-znakovu abecedu */
                /* !!!! this improvement works only on 2-letter alphabet */
                if (Variables.alphabet.size() != 2) {
                    try {
                        throw new Exception("You have to disable some optimisations due to alphabet size - look for !!!! in comments");
                    } catch (Exception ex) {
                        Logger.getLogger(AutomatonIterator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                /*------------------*/

                if (c.equals(Variables.alphabet.get(0))) {
                    it = new MatrixIterator(this.numberOfStates, this.TransitionMatrixIterators.get(Variables.alphabet.get(1)));
                }

                Matrix m = it.next();
                this.TransitionMatrixIterators.put(c, it);
                this.currentMatrices.put(c, m);
            } else {
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
