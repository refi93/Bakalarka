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
public class BigIntegerTuple{
    private final BigInteger first;
    private final BigInteger second;
    
    
    public BigIntegerTuple(BigInteger first, BigInteger second){
        this.first = first;
        this.second = second;
    }
    
    
    public BigInteger first(){
        return this.first;
    }
    
    
    public BigInteger second(){
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
        final BigIntegerTuple other = (BigIntegerTuple) obj;
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
    
    public static BigIntegerTuple minusOne(){
        return new BigIntegerTuple(BigInteger.valueOf(-1),BigInteger.valueOf(-1));
    }
}
