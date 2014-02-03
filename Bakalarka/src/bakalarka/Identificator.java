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
public class Identificator{
    private Integer value;
    int MAX_VAL = 31;
    
    
    public Identificator(Integer x){
        if (x <= MAX_VAL){
            this.value = x;
        }
        else{
            System.out.println("MAX VALUE OF INTEGER IDENTIFICATOR EXCEEDED");
        }
    }
    
    
    public Identificator(Identificator x){
        this.value = x.value;
    }
    
    
    public int getValue(){
        return this.value;
    }
    
    public String toString(){
        return value.toString();   
    }
    
    
    public Identificator copy() {
        return new Identificator(this.value);
    }
    
    
    /**
     *
     * @return hashCode, aky by mal Integer s touto hodnotou
     */
    public int hashCode(){
        return this.value;
    }

    
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Identificator other = (Identificator) obj;
        return this.value == other.value;
    }
}
