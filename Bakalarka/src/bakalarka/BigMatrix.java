package bakalarka;


import java.math.BigInteger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author raf
 */


/* tato trieda reprezentuje "velku" maticu - dost velku na pouzitie pri prevadzani 
minDFA na maticu a nasledne na BigInteger */

public class BigMatrix {
    int n;
    boolean [][] matrix;
    public BigMatrix(int n){
        this.n = n;
        this.matrix = new boolean[n][n];
        for(int r = 0;r < n;r++){
            for(int c = 0;r < n;r++){
                matrix[r][c] = false;
            }
        }
    }
    
    
    public void set(int r, int c, boolean val){
        this.matrix[r][c] = val;
    }
    
    
    // vrati maticu reprezentovanu ako jeden BigInteger
    public BigInteger getNumericRepresentation(){
        BigInteger ret = BigInteger.valueOf(0);
        for(int r = 0;r < n;r++){
            for(int c = 0;c < n;c++){
                if (matrix[r][c]){
                    ret = ret.add(BigInteger.valueOf(1).shiftLeft(n*r + c));
                }
            }
        }
        return ret;
    }
}
