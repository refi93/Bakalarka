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
    private final long first;
    private final long second;
    private final short third;
    
    
    public Triplet(long first, long second, short third){
        this.first = first;
        this.second = second;
        this.third = third;
    }
    
    
    public long first(){
        return this.first;
    }
    
    
    public long second(){
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
        return ((this.first == other.first()) && (this.second() == other.second()) && (this.third == other.third()) ); // mozme si to dovolit, lebo hashCode ma taku vlastnost
    }
    
    @Override
    public int hashCode(){
        //return (first.hashCode() ^ second.hashCode()) ^ third;
        int result = (int) (first ^ (first >>> 32));
        result = 31 * result + (int) (second ^ (second >>> 32));
        result = 31 * result + (int) ((long)third ^ (((long)third) >>> 32));
        return result;
    }
    
    
    @Override
    public String toString(){
        return this.first + " " + this.second + " " + this.third;
    }
    
    public static Triplet minusOne(){
        return new Triplet((long)-1,(long)-1,(short)-1);
    }
}
