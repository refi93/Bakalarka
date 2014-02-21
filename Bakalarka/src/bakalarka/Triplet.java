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
public class Triplet{
    private final BigInteger first;
    private final BigInteger second;
    private final Integer third;
    
    
    public Triplet(BigInteger first, BigInteger second, int third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    
    public BigInteger first(){
        return this.first;
    }
    
    
    public BigInteger second(){
        return this.second;
    }
    
    public int third(){
        return this.third;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Triplet other = (Triplet) obj;
        return ((this.first.equals(other.first())) && (this.second().equals(other.second())) && (this.third.equals(other.third()))); // mozme si to dovolit, lebo hashCode ma taku vlastnost
    }
    
    @Override
    public int hashCode(){
        //return (first.hashCode() ^ second.hashCode()) ^ third;
        /*int result = (int) (first.longValue() ^ (first.longValue() >>> 32));
        result = 31 * result + (int) (second.longValue() ^ (second.longValue() >>> 32));
        result = 31 * result + (int) ((long)third ^ (((long)third) >>> 32));*/
        
        int result = first.hashCode();
        result = 31 * result + second.hashCode();
        result = 31 * result + third.hashCode();
        return result;
    }
    
    
    @Override
    public String toString(){
        return this.first + " " + this.second + " " + this.third;
    }
    
    public static Triplet minusOne(){
        return new Triplet(BigInteger.valueOf(-1),BigInteger.valueOf(-1),-1);
    }
}
