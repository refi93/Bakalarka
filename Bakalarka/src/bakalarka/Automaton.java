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
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;


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
    private HashMap<Identificator,State> idStateMap; // tu si pamatame k idcku stav
    private HashSet<Identificator> allStatesIds; // mnozina idcok vsetkych stavov
    private HashSet<Identificator> finalStatesIds; // mnozina idciek akceptacnych stavov
    private HashSet<Identificator> initialStatesIds; // pociatocne stavy - pripusta sa ich viac
    // aj ked to neni celkom kosher, ale je to kvoli minimalizacii DKA cez reverz automatu
    private Identificator currentStateId; // aktualny stav

    
    public Automaton(){
        // parameterless konstruktor
        idStateMap = new HashMap<>();
        allStatesIds = new HashSet<>();
        finalStatesIds = new HashSet<>();
        initialStatesIds = new HashSet<>();
    }
    
    public Automaton(Automaton a){ // konstruktor ktoreho cielom je naklonovat automat a
        this.currentStateId = a.currentStateId.copy();
        
        
        this.allStatesIds = new HashSet<Identificator>();
        this.initialStatesIds = new HashSet<Identificator>();
        
        this.idStateMap = new HashMap<Identificator,State>();
        
        for (Identificator id : a.initialStatesIds){
            this.initialStatesIds.add(id.copy());
        }
        
        for (Identificator id : a.allStatesIds){
            this.allStatesIds.add(id.copy());
            this.idStateMap.put(id.copy(), a.idStateMap.get(id).copy());
        }
        
        this.finalStatesIds = new HashSet<Identificator>();
        for (Identificator id : a.finalStatesIds){
            this.finalStatesIds.add(id.copy());
        }
        
    }

    
    public Automaton(String s){
        // konstruktor Automatu dany Stringom
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.idStateMap);
        hash = 83 * hash + Objects.hashCode(this.allStatesIds);
        hash = 83 * hash + Objects.hashCode(this.finalStatesIds);
        hash = 83 * hash + Objects.hashCode(this.initialStatesIds);
        hash = 83 * hash + Objects.hashCode(this.currentStateId);
        return hash;
    }

  
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Automaton other = (Automaton) obj;
        return this.hashCode() == other.hashCode();
    }
    
    
    /* vykonanie prechodu automatu - funguje len pre deterministicke - pre
    nedeterministicke si to pozrie len prvu moznost - nevetvi sa to*/
    public boolean doTransition(Character input){
        if(!Variables.alphabet.contains(input)){ 
            throw new IllegalArgumentException();
        }
        
        currentStateId = idStateMap.get(currentStateId).get(input).iterator().next();

        if(currentStateId == null){ 
            throw new IllegalStateException(); 
        }
        
        System.out.println(currentStateId);
        return finalStatesIds.contains(currentStateId);
    }

    
    /*nastavenie pociatocneho stavu pomocou idcka stavu - tento stav sa nastavi
    aj ako current state*/
    public void setInitialState(Identificator stateId) throws NoSuchStateException{
        // nastavenie pociatocneho stavu
        if (idStateMap.get(stateId) != null){
            this.initialStatesIds.clear(); // vymazeme predosly pociatocny stav
            this.initialStatesIds.add(stateId);
            setCurrentState(stateId);
        }
        else{ // ak stav nie je v allStates
            throw new NoSuchStateException(stateId);
        }
    }
    
    
    /* nastavenie aktualneho stavu automatu */
    public void setCurrentState(Identificator stateId) throws NoSuchStateException{
        if (idStateMap.get(stateId) != null){
            this.currentStateId = stateId;
        }
        else{ // ak stav nie je v allStates
            throw new NoSuchStateException(stateId);
        }
    }
    
    
    public void addState(Identificator stateId) throws Exception{
        if (allStatesIds.contains(stateId)){
            //System.out.println(allStatesIds + "MARHA");
            throw new IdAlreadyExistsException(stateId);
        }
        State s = new State(stateId);
        idStateMap. put(stateId, s);
        allStatesIds.add(stateId);
    }
    
    /* pridanie prechodu do automatu prostrednictvom idciek stavov*/
    public void addTransition(Identificator idFrom, Identificator idTo, Character c) throws NoSuchStateException, Exception{
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
    public void addFinalState(Identificator stateId) throws NoSuchStateException{
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
    
    public State getState(Identificator id){
        return idStateMap.get(id);
    }
    
    public Automaton determinize() throws Exception{
        //TODO
        // determinizacia automatu
        Automaton pom = new Automaton(this); // naklonujeme nas automat
        
        Automaton ret = new Automaton(); // sem ulozime determinizovany automat
        
        // pociatocny stav determinizovaneho automatu je set obsahujuci pociatocne stavy nedeterminizovaneho
        PowerSetIdentificator retInitialStatesIds = new PowerSetIdentificator(this.initialStatesIds);
        
        ret.addState(retInitialStatesIds);
        
        ret.setInitialState(retInitialStatesIds);
        ret.setCurrentState(retInitialStatesIds);
        
        // fronta - v nej si pamatame este neexpandovane stavy
        Queue<PowerSetIdentificator> queue = new LinkedList<>();
        queue.add(retInitialStatesIds);
        
        
        while (!queue.isEmpty()){
            // vyberieme z fronty prvy stav
            PowerSetIdentificator currentRetId = queue.peek();
            
            for (Character c : Variables.alphabet){ // prechadzame moznymi znakmi abecedy
                PowerSetIdentificator newId = new PowerSetIdentificator();
                boolean thisIsFinalState = false; 
                
                // prechadzame cez stavy stareho automatu obsiahnute v stave co prave expandujeme
                for (Identificator IdentificatorInPom : currentRetId){
                    
                    if (pom.getState(IdentificatorInPom).getTransition(c) != null){
                        for(Identificator identificatorOfTransitionState : pom.getState(IdentificatorInPom).getTransition(c)){
                            if (pom.finalStatesIds.contains(identificatorOfTransitionState)){
                                thisIsFinalState = true;
                            }
                            newId.add(identificatorOfTransitionState);
                        }
                    }
                }
                if (!newId.isEmpty()){
                    try{
                        ret.addState(newId);
                        queue.add(newId);
                        if (thisIsFinalState){
                            ret.finalStatesIds.add(newId);
                        }
                    }
                    catch(Exception e){
                        System.out.println("ale nic");
                    }
                    ret.addTransition(currentRetId, newId, c);
                }
            }
            queue.remove();
        }
        
        System.out.println(ret.allStatesIds);
        
        return ret;
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