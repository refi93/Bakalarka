/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author raf
 */

/* trieda reprezentujuca stvorcovu maticu nxn */
public class Matrix {
    boolean[][] matrix;
    int n;
    private BigInteger numericRepresentation; // matica reprezentovana ako cislo
    
    // konstruktor nulovej stvorcovej matice nxn
    // dohoda: matica[r][c] znamena pritomnost orientovanej hrany r -> c
    public Matrix(int n){
        this.numericRepresentation = BigInteger.valueOf(0);
        this.matrix = new boolean[n][n];
        this.n = n;
        for(int i = 0;i < n;i++){
            for(int j = 0;j < n;j++){
                matrix[i][j] = false;
            }
        }
    }
    
    /* konstruktor dany rozmerom matice a cislom, z ktoreho sa nasekaju
    jednotlive riadky matice
    */
    public Matrix(int n, BigInteger numericRepresentation){
        this.numericRepresentation = numericRepresentation;
        this.matrix = new boolean[n][n];
        this.n = n;
        
        for(int i = 0;i < n;i++){
            for(int j = 0;j < n;j++){
                // numericRepresentation % 2 == 1
                this.matrix[i][j] = (numericRepresentation.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(1)));
                numericRepresentation = numericRepresentation.shiftRight(1);
            }
        }
    }
    
    
    public void set(int r, int c, boolean val){
        //long prev = (long)(((long)1 << (n*r + c)) * (this.matrix[r][c]?1:0));
        this.matrix[r][c] = val;
        BigInteger offset = BigInteger.valueOf(1).shiftLeft(n * r + c);
        if(val){ // ak ideme nastavovat true
            this.numericRepresentation = this.numericRepresentation.or(offset); 
        }
        else{ // ak ideme nastavovat false, tak spravime bitovy and
            if (this.get(r, c)){
                this.numericRepresentation = this.numericRepresentation.subtract(offset);
            }
        }
    }
    
    
    /* vrati to reprezentaciu matice ako cele cislo */
    public BigInteger getNumericRepresentation(){
        return  this.numericRepresentation;
    }
    
    
    public boolean get(int r, int c){
        return this.matrix[r][c];
    }
    
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("\n");
        for (int i = 0;i < this.n;i++){
            for (int j = 0;j < this.n;j++){
                if (this.matrix[i][j]){
                    ret .append("1 ");
                }
                else{
                    ret.append("0 ");
                }
            }
            ret.append("\n");
        }
        return ret.toString();
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
    
    boolean isConnected(){
        Queue<Integer> queue = new LinkedList<>();
        HashSet<Integer> seen = new HashSet<>();
        queue.add(0); 
        seen.add(0);
        
        while(!queue.isEmpty()){
            int current = queue.remove();
            for(int i = 0;i < this.matrix[current].length;i++){
                if ((matrix[current][i]) && (!seen.contains(i))){
                    seen.add(i);
                    queue.add(i);
                }
            }
        }
        
        for(int i = 0;i < this.n;i++){
            if (!seen.contains(i)) return false;
        }
        
        return true;
    }
    
    Matrix union(Matrix other){
        return new Matrix(this.n, other.numericRepresentation.or(this.numericRepresentation));
    }
    
    
    /* predpoklad pre tuto metodu je, ze ta matica patri DFA a ze je najviac 16x16 */
    public BigInteger getNumericRepresentationDFA(){
        //if (n > 16) System.out.println("WARNING: MAXIMAL MATRIX SIZE EXCEEDED");
        
        BigInteger ret = BigInteger.valueOf(0);
        for(int r = 0;r < n;r++){
            for(int c = 0;c < n;c++){
                if (matrix[r][c]){
                    ret = ret.or(BigInteger.valueOf(1).shiftLeft(n * r + c));
                }
            }
        }
        return ret;
    }
}
