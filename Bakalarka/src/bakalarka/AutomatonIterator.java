/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author raf
 */
public class AutomatonIterator implements Iterator{
    
    // tu sa budu postupne generovat vsetky mozne matice prechodov
    HashMap<Character, MatrixIterator> allTransitionsIterator;
    // tu sa budu postupne generovat vsetky mozne mnoziny akceptacnych stavov
    SubsetIterator allSubsetsIterator;
    // iterator postupne cez vsetky mozne stavy
    IntegerIterator allStatesIterator;

    HashMap<Character,Matrix> currentTransitions;
    HashSet<Identificator> currentFinalStatesIds;
    IntegerIdentificator currentInitialStateId;
    // pocet stavov, ktory uvazujeme
    Integer numberOfStates;
    boolean hasNext;
    
    
    public AutomatonIterator(int n) throws Exception{
        if (n == 0) {
            this.numberOfStates = 0;
            return;
        }
        this.hasNext = true;
        this.numberOfStates = n;
        
        this.allTransitionsIterator = new HashMap<>();
        this.currentTransitions = new HashMap<>();
        for(Character c : Variables.alphabet){
            MatrixIterator it = new MatrixIterator(n);
            this.currentTransitions.put(c, it.next());
            this.allTransitionsIterator.put(c, it);
        }
        
        this.allSubsetsIterator = new SubsetIterator(n);
        this.currentFinalStatesIds = this.allSubsetsIterator.next();
        
        // staci uvazovat len stav 0 ako pociatocny, ostatne pripady su len nejake izomorfne k tomu
        this.allStatesIterator = new IntegerIterator(1);
        this.currentInitialStateId = this.allStatesIterator.next();
    }
    
    
    /* vrati nasledujuci automat s danym poctom stavov v poradi */
    @Override
    public Automaton next(){
        if (!this.hasNext()) return null;
        this.checkNext();
        Automaton ret = new Automaton();
        try {
            ret = new Automaton(this.currentTransitions,this.currentInitialStateId,this.currentFinalStatesIds);
        } catch (Exception ex) {
            Logger.getLogger(AutomatonIterator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!this.allStatesIterator.hasNext()){
            this.allStatesIterator = new IntegerIterator(1);
            this.currentInitialStateId = this.allStatesIterator.next();
            
            if(!this.allSubsetsIterator.hasNext()){
                this.allSubsetsIterator = new SubsetIterator(this.numberOfStates);
                this.currentFinalStatesIds = this.allSubsetsIterator.next();
                for(Character c : Variables.alphabet){
                    if(!this.allTransitionsIterator.get(c).hasNext()){
                        MatrixIterator it = new MatrixIterator(this.numberOfStates);
                        this.currentTransitions.put(c, it.next());
                        this.allTransitionsIterator.put(c,it);
                    }
                    else{
                        this.currentTransitions.put(c, this.allTransitionsIterator.get(c).next());
                        break;
                    }
                }
            }
            else{
                this.currentFinalStatesIds = this.allSubsetsIterator.next();
            }
        }
        else{
            this.currentInitialStateId = this.allStatesIterator.next();
        }
        
        return ret;
    }
    
    
    
    /* na zistenie, ci este mame nieco vypisat */
    private boolean checkNext(){
        boolean ret = this.hasNext;
        if ((this.allStatesIterator.hasNext()) || (this.allSubsetsIterator.hasNext())){
            hasNext = true;
            return hasNext;
        }
        for(Character c : Variables.alphabet){
            if (this.allTransitionsIterator.get(c).hasNext()) {
                hasNext = true;
                return hasNext;
            }
        }
        hasNext = false;
        return ret;
    }
    
    @Override
    public boolean hasNext(){
        if (this.numberOfStates == 0) return false;
        return hasNext;
    }
    
    public Automaton random() throws Exception{
        MatrixIterator matrixIt = new MatrixIterator(this.numberOfStates);
        SubsetIterator subsetIt = new SubsetIterator(this.numberOfStates);
        HashMap<Character,Matrix> transitions = new HashMap<>();
        
        for (Character c : Variables.alphabet){
            transitions.put(c,matrixIt.random());
        }
        
        return new Automaton(transitions,new IntegerIdentificator(0),subsetIt.random());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
