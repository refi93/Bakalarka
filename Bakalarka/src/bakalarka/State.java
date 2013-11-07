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
 * tato trieda reprezentuje stav automatu - k znaku si pamatame mnozinu 
 * stavov, na ktore sa da prejst
 */

public class State extends HashMap<Character, ArrayList<State> >{
    
    /* vrati, ci je dany stav "deterministicky", teda ci je jednoznacne 
    urcene, co bude nasledovat
    */
    public boolean isDeterministic(){
        for(Character c: Variables.alphabet){
            if ((this.get(c) != null) && (this.get(c).size() > 1)){
                return false;
            }
        }
        return true; 
    }
    
    /* pridanie prechodu do stavu*/
    public void addTransition(Character c,State s){
        ArrayList value = this.get(c);
        if (value == null){
            value = new ArrayList<>();
        }
        value.add(s);
        this.put(c, value);
    }
}
