/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.HashMap;

/**
 *
 * @author raf
 */

public class State extends HashMap<Character, State>{
    
    /* pridanie prechodu do stavu*/
    public void addTransition(Character c,State s){
        this.put(c,s);
    }
}
