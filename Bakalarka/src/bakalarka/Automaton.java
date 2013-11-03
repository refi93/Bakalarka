/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author raf
 */

class NoSuchStateException extends Exception
{
      //Parameterless Constructor
      public NoSuchStateException(State s) {
          System.out.println("No such state" + s. toString());
      }

      //Constructor that accepts a message
      public NoSuchStateException(String message)
      {
         super(message);
      }
}

class IdAlreadyExistsException extends Exception
{
      //Parameterless Constructor
      public IdAlreadyExistsException(int id) {
          System.out.println("ID " + id + " ALREADY EXISTS");
      }

      //Constructor that accepts a message
      public IdAlreadyExistsException(String message)
      {
         super(message);
      }
}


public class Automaton{
    private HashMap<Integer,State> idStateMap; // tu si pamatame k idcku stav
    private HashMap<State,Integer> stateIdMap; // tu si pamatame k stavu idcko
    private HashSet<State> allStates; // mnozina vsetkych stavov
    private HashSet<State> finalStates; // mnozina akceptacnych stavov
    private State initialState; // pociatocny stav
    private State currentState; // aktualny stav
    private HashSet<Character> alphabet = new HashSet(Arrays.asList(new Character[] { '0', '1'}))  ; // abeceda - pre nase ucely {0,1}

    
    public Automaton(Automaton a){
        // konstruktor dany dalsim automatom
    }
    
    
    public Automaton(ArrayList<State> states){
        // konstruktor dany mnozinou stavov
    }
    
    
    public Automaton(String s){
        // konstruktor Automatu dany Stringom
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

    
    /*nastavenie pociatocneho stavu pomocou premennej typu State*/
    private void setInitialState(State s) throws NoSuchStateException{
        // nastavenie pociatocneho stavu
        if (allStates.contains(s)){
            this.initialState = s;
        }
        else{ // ak stav nie je v allStates
            throw new NoSuchStateException(s);
        }
    }
    
    
    /*nastavenie pociatocneho stavu pomocou idcka stavu*/
    public void setInitialState(int id) throws NoSuchStateException{
        try{
            State s = idStateMap.get(id);
            setInitialState(s);
        }
        catch(NoSuchStateException e){
            throw new NoSuchStateException("ERROR: setting initial state failed");
        }
    }
    
    
    public void addState(int stateId) throws Exception{
        if (idStateMap.containsKey(stateId)){
            throw new IdAlreadyExistsException(stateId);
        }
        State s = new State();
        idStateMap. put(stateId, s);
        stateIdMap. put(s, stateId);
    }
    
    /* pridanie prechodu do automatu prostrednictvom idciek stavov*/
    public void addTransition(int idFrom, int idTo, Character c) throws NoSuchStateException{
        State from = idStateMap. get(idFrom);
        State to = idStateMap. get(idTo);
       
        if ((from != null) && (to != null)){
            from.addTransition(c, to);
    
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD TRANSITION");
        }
    }
    
    
    /* pridanie konecneho stavu */
    private void addFinalState(State s){
        finalStates.add(s);
    }
    
    
    /* pridanie akceptacneho stavu prostrednictvom idcka */
    public void addFinalState(int stateId) throws NoSuchStateException{
        State s = idStateMap.get(stateId);
        if (s != null){
            addFinalState(s);
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD FINAL STATE");
        }
    }
    
    /* otestuje, ci ide o totozny automat */
    public boolean equals(Automaton b){
       // TODO
       return true;
    }
    
    /* otestuje, ci ide o ekvivalentny automat */
    public boolean equiv(Automaton b){
        // TODO
        return false;
    }
    
    public void determinize(){
        //TODO
        // determinizacia automatu
    }
    
    
    @Override
    public String toString(){
        // TODO
        return "NOT YET IMPLEMENTED";
    }
    
    
    /* overi, ci je dany automat deterministicky */
    public boolean isDeterministic(){
        // TODO
        return false;
    }
    // more methods go here
}     