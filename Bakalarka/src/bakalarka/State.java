/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author raf
 * tato trieda reprezentuje stav automatu - k znaku si pamatame mnozinu idciek 
 * stavov, na ktore sa da prejst
 */

public class State extends HashMap<Character, HashSet<Identificator> >{
    
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
        return "state " + id.toString();
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
    
    
    /* vrati true ak je dany stav deterministicky, t.j. pre kazde psimeno ma 
    urceny prave jeden prechod, inak to vrati false */
    public boolean isDeterministic(){
        for(Character c: Variables.alphabet){
            if ((this.get(c) == null) || (this.get(c).size() != 1)){
                return false;
            }
        }
        return true; 
    }
    
    
    /* pridanie prechodu do stavu*/
    public void addTransition(Character c,Identificator stateId){
        HashSet value = this.get(c);
        if (value == null){
            value = new HashSet<>();
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
    
    
    /* ziskame stav, na ktory prejdeme, ked dostaneme znak c */
    HashSet<Identificator> getTransition(Character c){
        return this.get(c);
    }
    
    
    /* zmeni identifikator stavu na zadany identifikator */
    public void setIdentificator(Identificator id){
        this.id = id;
    }
    
    
    /* touto metodou dame stavu vediet, ze v automate sa premenoval nejaky stav */
    public void replaceStateId(Identificator oldId, Identificator newId){
        if (this.id.equals(oldId)){ // ak je to prave tento stav, tak ho premenujeme
            this.setIdentificator(newId);
        }
        for (Character c : Variables.alphabet){ // vsetky transitions prekontrolujeme a tie neaktualne premenujeme
            if (this.get(c) != null){
                if(this.get(c).contains(oldId)){
                    this.get(c).remove(oldId);
                    this.get(c).add(newId);
                }
            }
        }
    }
}
