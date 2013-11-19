/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.Iterator;

/**
 *
 * @author raf
 */


/* trieda reprezentujuca vsetky matice rozmeru n x n */
public class MatrixIterator implements Iterator<Matrix>{
    int n;
    long state;
    
    
    public MatrixIterator(int n){
        this.n = n;
        state = 0;
    }
    
    
    /* vrati nasledujucu maticu v lexikografickom poradi */
    @Override
    public Matrix next(){
        if (!this.hasNext()) return null;
        Matrix ret = new Matrix(n);
        long pom = state;
        for(int i = 0;i < n;i++){
            for(int j = 0;j < n;j++){
                ret.set(i, j, (pom % 2 == 1));
                pom /= 2;
            }
        }
        state++;
        return ret;
    }

    
    @Override
    public boolean hasNext(){
        return (state < Math.pow(2, n * n));
    }
    
    
    /* metoda na skopirovanie AllMatrices iteratora */
    public MatrixIterator copy(){
        MatrixIterator ret = new MatrixIterator(this.n);
        ret.state = this.state;
        return ret;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
