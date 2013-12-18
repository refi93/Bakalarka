/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author raf
 */

/* trieda reprezentujuca stvorcovu maticu nxn velkosti max 7x7 kvoli obmedzeniam longu */
public class Matrix {
    boolean[][] matrix;
    int n;
    private long numericRepresentation; // matica reprezentovana ako cislo
    
    // konstruktor nulovej stvorcovej matice nxn
    // dohoda: matica[r][c] znamena pritomnost orientovanej hrany r -> c
    public Matrix(int n){
        if (n > 7){
            System.out.println("WARNING: MAXIMAL MATRIX SIZE (7x7) EXCEEDED");
        }
        this.numericRepresentation = 0;
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
    public Matrix(int n, long numericRepresentation){
        this.numericRepresentation = numericRepresentation;
        this.matrix = new Matrix(n).matrix;
        this.n = n;
        
        for(int i = 0;i < n;i++){
            for(int j = 0;j < n;j++){
                this.matrix[i][j] = (numericRepresentation % 2 == 1);
                numericRepresentation /= 2;
            }
        }
    }
    
    
    public void set(int r, int c, boolean val){
        long prev = (long)(((long)1 << (n*r + c)) * (this.matrix[r][c]?1:0));
        this.matrix[r][c] = val;
        this.numericRepresentation = this.numericRepresentation - prev + (long)(((long)1 << (n*r + c))* (val?1:0));
    }
    
    
    /* vrati to reprezentaciu matice ako cele cislo */
    public long getNumericRepresentation(){
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
        //System.out.println(other.numericRepresentation);
        //System.out.println(this.numericRepresentation);
        //System.out.println(other.numericRepresentation | this.numericRepresentation);
        return new Matrix(this.n, other.numericRepresentation | this.numericRepresentation);
    }
}
