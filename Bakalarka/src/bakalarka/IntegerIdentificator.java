/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

/**
 *
 * @author raf
 * identifikator stavu dany cislom
 */
public class IntegerIdentificator implements Identificator{
    private Integer value;
    int MAX_VAL = 31;
    
    
    public IntegerIdentificator(Integer x){
        if (x <= MAX_VAL){
            this.value = x;
        }
        else{
            System.out.println("MAX VALUE OF INTEGER IDENTIFICATOR EXCEEDED");
        }
    }
    
    
    public IntegerIdentificator(IntegerIdentificator x){
        this.value = x.value;
    }
    
    
    public int getValue(){
        return this.value;
    }
    
    @Override
    public String toString(){
        return value.toString();   
    }
    
    
    @Override
    public Identificator copy() {
        return new IntegerIdentificator(this.value);
    }
    
    
    /**
     *
     * @return hashCode, aky by mal Integer s touto hodnotou
     */
    @Override
    public int hashCode(){
        return this.value;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IntegerIdentificator other = (IntegerIdentificator) obj;
        return this.value == other.value;
    }
}
