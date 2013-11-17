/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

/**
 *
 * @author raf
 */


/* trieda reprezentujuca vsetky matice rozmeru n x n */
public class MatrixIterator {
    int n;
    long state;
    
    
    public MatrixIterator(int n){
        this.n = n;
        state = 0;
    }
    
    
    /* vrati nasledujucu maticu v lexikografickom poradi */
    public Matrix next() throws Exception{
        if (!this.hasNext()) throw new Exception("Iterator overflow");
        Matrix ret = this.current();
        state++;
        return ret;
    }
    
    
    /* ako next, len nezmeni to state */
    public Matrix current() throws Exception{
        Matrix ret = new Matrix(n);
        long pom = state;
        for(int i = 0;i < n;i++){
            for(int j = 0;j < n;j++){
                ret.set(i, j, (pom % 2 == 1));
                pom /= 2;
            }
        }
        return ret;
    }
    
    public boolean hasNext(){
        return (state < Math.pow(2, n * n) - 1);
    }
    
    
    /* metoda na skopirovanie AllMatrices iteratora */
    public MatrixIterator copy(){
        MatrixIterator ret = new MatrixIterator(this.n);
        ret.state = this.state;
        return ret;
    }
}
