/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author raf
 */

public class Automaton{ 
    private Set<State> allStates;
    private Set<State> finalStates;
    private State initialState;
    private State currentState;
    private Set<Character> alphabet;

    public Automaton(){
        // konstruktor
    }
    
    public boolean doTransition(Character input){
        if(!alphabet.contains(input)){ 
            throw new IllegalArgumentException();
        }
        if(finalStates.contains(currentState)){ 
            throw new IllegalStateException(); 
        }

        currentState = currentState.get(input);

        if(currentState == null){ 
            throw new IllegalStateException(); 
        }

        return finalStates.contains(currentState);
    }

    public void addState(){
        // pridanie stavu do automatu
    }
    
    public void editState(){
        // upravenie stavu automatu
    }
    
    public boolean Equals(Automaton b){
       // TODO
       return true;
    }
    
    public void Determinize(){
        // determinizacia automatu
    }
    // more methods go here
}     