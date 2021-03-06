/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author raf
 * identifikator stavu dany potencnou mnozinou
 * je to mozno fuj, ale su tam pridane optimalizacie specialne pre pripad, ze ide
 * o PowerSetIdentificator obsahujuci len IntegerIdentificatory
 */
public class SetOfIdentificators extends HashSet<Identificator> {

    int hash_cache = -1;
    
    // ci su tam len integerIdentificatory - kvoli lepsiemu rataniu hashCode
    private boolean hasIntegerIdentificatorsOnly = true;
    
    // bitmapa kde si pamatame identifikatory, pokial su to integerIdentificatory
    private int bitMap = 0; 
    private int size = 0;
    
    public SetOfIdentificators(HashSet<Identificator> statesSet){
        super();
        for (Identificator id : statesSet){
            this.add(id);
        }
    }
    
    public SetOfIdentificators(){
        super();
    }
    

    public SetOfIdentificators copy() {
        SetOfIdentificators ret = new SetOfIdentificators();
        
        ret.bitMap = this.bitMap;
        ret.hasIntegerIdentificatorsOnly = this.hasIntegerIdentificatorsOnly;
        if (!this.hasIntegerIdentificatorsOnly){ 
// pokial tam nejsu len integerIdentificatory - pre ktore to je optimalizovane tak nepomozeme si - proste ich tam nakopirujeme vsetky natvrdo
            for(Identificator x : this){
                ret.add(x.copy());
            }
        }
        ret.hash_cache = this.hash_cache;
        return ret;
    }

    @Override
    public boolean add(Identificator x){
        this.hash_cache = -1;
        boolean ret = (((this.bitMap >> x.getValue()) % 2) == 0);
        this.bitMap = this.bitMap | (1 << x.getValue());
        if (ret) size++;
        return ret;
    }
    
    @Override
    public boolean contains(Object obj){
        if(this.hasIntegerIdentificatorsOnly){
            // pokial su tu len IntegerIdentificatory, tak pouzivame bitMapu
            if(obj.getClass() == Identificator.class){
                return ((this.bitMap >> ((Identificator)obj).getValue()) % 2 == 1);
            }
            else{
                return false;
            }
        }
        return super.contains(obj);
    }
    

    @Override
    public int size(){
        return this.size;
    }
    
    
    @Override
    public boolean isEmpty(){
        return (this.size == 0);
    }
    
    
    @Override
    public Iterator<Identificator> iterator(){
        if(!this.hasIntegerIdentificatorsOnly) return super.iterator();
        
        
        // pre powerSet IntegerOdentificatorov treba definovat vlastny iterator, lebo si prvky pamata v bitMape
        Iterator<Identificator> it = new Iterator<Identificator>(){

            private int state = bitMap;
            private int index = 0;
            @Override
            public boolean hasNext() {
                return (state > 0);
            }

            @Override
            public Identificator next() {
                while(state % 2 != 1){
                    state = state >> 1;
                    index++;
                }
                state = state >> 1;
                return new Identificator(index++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        return it;
    }
    
    @Override
    public int hashCode(){
        if (hash_cache != -1){
            return hash_cache;
        }
        else{
            // pokial to je PowerSetIndentificator niecoho ineho
            if (!hasIntegerIdentificatorsOnly){
                hash_cache = super.hashCode();
                return hash_cache;
            }
            
            hash_cache = this.bitMap;
            return hash_cache;
        }
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SetOfIdentificators other = (SetOfIdentificators) obj;
        
        // pokial oba powerSety maju len integerIdentificatory
        if (other.hasIntegerIdentificatorsOnly && this.hasIntegerIdentificatorsOnly){
            // vtedy mame zaruku, ze hashCode dokaze urcit rovnost s istotou
            if (other.hashCode() == this.hashCode()) return true;
            return false;
        }
        
        
        if (other.hashCode() != this.hashCode()) return false;
        return super.equals(other);
    }
    
    
    public int getBitMap() throws Exception{
        if (this.hasIntegerIdentificatorsOnly){
            return this.bitMap;
        }
        else{
            throw new Exception("THIS POWER SET DOES NOT CONTAIN BIT MAP");
        }
    }
    
}
