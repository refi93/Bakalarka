/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author raf
 */

class NoSuchStateException extends Exception
{
      //Parameterless Constructor
      public NoSuchStateException(Object stateId) {
          System.out.println("No such state" + stateId. toString());
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
      public IdAlreadyExistsException(Object id) {
          System.out.println("ID " + id + " ALREADY EXISTS");
      }

      //Constructor that accepts a message
      public IdAlreadyExistsException(String message)
      {
         super(message);
      }
}


public class Automaton{
    private HashMap<Object,State> idStateMap; // tu si pamatame k idcku stav
    private HashSet<Object> allStatesIds; // mnozina idcok vsetkych stavov
    private HashSet<Object> finalStatesIds; // mnozina idciek akceptacnych stavov
    private Object initialStateId; // pociatocny stav
    private Object currentStateId; // aktualny stav

    
    public Automaton(){
        // parameterless konstruktor
        idStateMap = new HashMap<>();
        allStatesIds = new HashSet<>();
        finalStatesIds = new HashSet<>();
    }
    
    public Automaton(Automaton a){
        
    }
    
    
    public Automaton(ArrayList<State> states){
        // konstruktor dany mnozinou stavov
    }
    
    
    public Automaton(String s){
        // konstruktor Automatu dany Stringom
    }
    
    /* vykonanie prechodu automatu - funguje len pre deterministicke - pre
    nedeterministicke si to pozrie len prvu moznost - nevetvi sa to*/
    public boolean doTransition(Character input){
        if(!Variables.alphabet.contains(input)){ 
            throw new IllegalArgumentException();
        }
        
        currentStateId = idStateMap.get(currentStateId).get(input).get(0);

        if(currentStateId == null){ 
            throw new IllegalStateException(); 
        }
        
        System.out.println(currentStateId);
        return finalStatesIds.contains(currentStateId);
    }

    
    /*nastavenie pociatocneho stavu pomocou idcka stavu - tento stav sa nastavi
    aj ako current state*/
    public void setInitialState(Object stateId) throws NoSuchStateException{
        // nastavenie pociatocneho stavu
        if (idStateMap.get(stateId) != null){
            this.initialStateId = stateId;
            setCurrentState(stateId);
        }
        else{ // ak stav nie je v allStates
            throw new NoSuchStateException(stateId);
        }
    }
    
    
    /* nastavenie aktualneho stavu automatu */
    public void setCurrentState(Object stateId) throws NoSuchStateException{
        if (idStateMap.get(stateId) != null){
            this.currentStateId = stateId;
        }
        else{ // ak stav nie je v allStates
            throw new NoSuchStateException(stateId);
        }
    }
    
    
    public void addState(Object stateId) throws Exception{
        if (idStateMap.containsKey(stateId)){
            throw new IdAlreadyExistsException(stateId);
        }
        State s = new State(stateId);
        idStateMap. put(stateId, s);
        allStatesIds.add(stateId);
    }
    
    /* pridanie prechodu do automatu prostrednictvom idciek stavov*/
    public void addTransition(Object idFrom, Object idTo, Character c) throws NoSuchStateException, Exception{
        State from = idStateMap. get(idFrom);
        State to = idStateMap. get(idTo);
        
        // vymazeme stare zaznamy, aby sme ich nasledne aktualizovali
        idStateMap.remove(idFrom);
        
        if ((from != null) && (to != null)){
            from.addTransition(c, idTo);
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD TRANSITION");
        }
        
        // vlozime naspat aktualizovany zaznam
        idStateMap.put(idFrom, from);
    }
    
    
    /* pridanie akceptacneho stavu prostrednictvom idcka */
    public void addFinalState(Object stateId) throws NoSuchStateException{
        State s = idStateMap.get(stateId);
        if (s != null){
            finalStatesIds.add(s.id);
        }
        else{
            throw new NoSuchStateException("FAILED TO ADD FINAL STATE");
        }
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
        for (Object stateId : allStatesIds) {
            if (!idStateMap.get(stateId).isDeterministic()){
                return false;
            }
        }
        return true;
    }
    
    /* vrati pocet stavov automatu */
    public int getNumberOfStates(){
        return allStatesIds.size();
    }
    // more methods go here
}     