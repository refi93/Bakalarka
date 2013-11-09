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
    int value;
    
    public IntegerIdentificator(int x){
        this.value = x;
    }
    
    public IntegerIdentificator(IntegerIdentificator x){
        this.value = x.value;
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
        return (new Integer(this.value)).hashCode();
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
        if (this.value != other.value) {
            return false;
        }
        return true;
    }
}
