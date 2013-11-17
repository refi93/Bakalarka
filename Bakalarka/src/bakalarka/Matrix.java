/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.Arrays;

/**
 *
 * @author raf
 */

/* trieda reprezentujuca stvorcovu maticu nxn */
public class Matrix {
    boolean[][] matrix;
    int n;
    
    // konstruktor stvorcovej matice nxn
    public Matrix(int n){
        this.matrix = new boolean[n][n];
        this.n = n;
        for(int i = 0;i < n;i++){
            for(int j = 0;j < n;j++){
                matrix[i][j] = false;
            }
        }
    }
    
    
    public void set(int r, int c, boolean val){
        this.matrix[r][c] = val; 
    }
    
    
    public boolean get(int r, int c){
        return this.matrix[r][c];
    }
    
    @Override
    public String toString(){
        String ret = new String();
        for (int i = 0;i < this.n;i++){
            for (int j = 0;j < this.n;j++){
                if (this.matrix[i][j]){
                    System.out.print("1 ");
                }
                else{
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
        return ret;
    }
    

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Matrix other = (Matrix) obj;
        if (!Arrays.deepEquals(this.matrix, other.matrix)) {
            return false;
        }
        return this.n == other.n;
    }
}
