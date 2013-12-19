/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.math.BigInteger;

/**
 *
 * @author raf
 */
public class Tuple <X,Y>{
    private final X first;
    private final Y second;
    
    
    public Tuple(X first, Y second){
        this.first = first;
        this.second = second;
    }
    
    
    public X first(){
        return this.first;
    }
    
    
    public Y second(){
        return this.second;
    }
    
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuple other = (Tuple) obj;
        return ((this.first().equals(other.first()))&&(this.second().equals(other.second()))); // mozme si to dovolit, lebo hashCode ma taku vlastnost
    }
    
    @Override
    public int hashCode(){
        return first.hashCode() + second.hashCode() * 31;
    }
    
    
    @Override
    public String toString(){
        return this.first.toString() + " " + this.second.toString();
    }
    
    public static Tuple minusOne(){
        return new Tuple(BigInteger.valueOf(-1),BigInteger.valueOf(-1));
    }
}