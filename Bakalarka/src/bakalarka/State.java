/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author raf
 * tato trieda reprezentuje stav automatu - k znaku si pamatame mnozinu idciek 
 * stavov, na ktore sa da prejst
 */

public class State extends HashMap<Character, ArrayList<Identificator> >{
    
    /* vrati, ci je dany stav "deterministicky", teda ci je jednoznacne 
    urcene, co bude nasledovat
    */
    
    Identificator id; // idcko stavu
    
    public State(Identificator id){
        super();
        this.id = id;
    }

    @Override
    public String toString(){
        return "MARHA";
    }
    
    public State copy(){
        State ret = new State(this.id.copy());
        for(Character c : Variables.alphabet){
            if (this.get(c) != null){
                for(Identificator transitionStateId : this.get(c)){
                    ret.addTransition(c, transitionStateId.copy());
                }
            }
        }
        return ret;
    }
    
    public boolean isDeterministic(){
        for(Character c: Variables.alphabet){
            if ((this.get(c) != null) && (this.get(c).size() > 1)){
                return false;
            }
        }
        return true; 
    }
    
    /* pridanie prechodu do stavu*/
    public void addTransition(Character c,Identificator stateId){
        ArrayList value = this.get(c);
        if (value == null){
            value = new ArrayList<>();
        }
        value.add(stateId);
        this.put(c, value);
    }
    
    public boolean equals(State s){
        return s.id.equals(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        return other.id.equals(this.id);
    }
    
    @Override
    public int hashCode(){
        return id.hashCode();
    }
}
