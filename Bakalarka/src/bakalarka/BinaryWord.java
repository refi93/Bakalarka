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
 * tato trieda sluzi na definovanie slova v 2-pismenkovej abecede
 */
public class BinaryWord {
    public int value;
    public int length;
    
    // tato trieda tu je len kvoli hashovaniu, tak preto nastavime max dlzku takto
    private int MAX_LEN = Variables.HashWordLength;
    
    
    /* prazdny konstruktor */
    public BinaryWord(){
        this.value = 0;
        this.length = 0;
    }
    
    
    /* konstruktor dany inym BinaryWordo-om */
    public BinaryWord(BinaryWord other){
        this.length = other.length;
        this.value = other.value;
    }
    
    
    /* pridanie znaku na koniec */
    public BinaryWord append(int val) throws Exception{
        if (length > 32){
            throw new Exception("MAXIMAL LENGTH EXCEEDED");
        }
        if (val > 1){
            throw new Exception("YOU ARE TRYING TO INSERT NON BINARY CHARACTER");
        }
        this.value = this.value + (val << this.length);
        length++;
        return this;
    }
    
    
    /* odobratie posledneho znaku */
    public BinaryWord cutLast(){
        if (this.length == 0) return this;
        this.length--;
        this.value = (this.value | (1 << this.length)) - (1 << this.length);
        return this;
    }
    
    
    /* hashCode je vlastne prenesenie tohto cisla do 3-kovej sustavy, nech aj 
    nuly zavazia
    */
    @Override
    public int hashCode(){
        int ret = this.value + (1 << this.length);
        return ret;
    }
    
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BinaryWord other = (BinaryWord) obj;
        return (this.hashCode() == other.hashCode()); // mozme si to dovolit, lebo hashCode ma taku vlastnost
    }
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        for(int i = 0;i < this.length;i++){
            ret.append((this.value >> i) % 2);
        }
        return ret.toString();
    }
    
}
