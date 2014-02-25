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
public class Tuple{
    private final BigInteger first;
    private final Integer second;
    
    
    public Tuple(BigInteger first, int second){
        this.first = first;
        this.second = second;
    }
    
    
    public BigInteger first(){
        return this.first;
    }
    
    
    public int second(){
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
        return ((this.first.equals(other.first())) && (this.second.equals(other.second()))); // mozme si to dovolit, lebo hashCode ma taku vlastnost
    }
    
    @Override
    public int hashCode(){
        //return (first.hashCode() ^ second.hashCode()) ^ third;
        /*int result = (int) (first.longValue() ^ (first.longValue() >>> 32));
        result = 31 * result + (int) (second.longValue() ^ (second.longValue() >>> 32));
        result = 31 * result + (int) ((long)third ^ (((long)third) >>> 32));*/
        
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
    
    
    @Override
    public String toString(){
        return this.first + " " + this.second;
    }
    
    public static Tuple minusOne(){
        return new Tuple(BigInteger.valueOf(-1),-1);
    }
}
