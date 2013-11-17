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

/* trieda reprezentujuca stvorcovu maticu nxn */
public class Matrix {
    public boolean[][] matrix;
    
    // konstruktor stvorcovej matice nxn
    public Matrix(int n){
        this.matrix = new boolean[n][n];
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
    
}
