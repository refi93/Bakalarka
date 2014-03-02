/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.IOException;
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
    
    // tu sa budu postupne generovat vsetky mozne mnoziny akceptacnych stavov
    SubsetIterator allSubsetsIterator;
    // iterator postupne cez vsetky mozne stavy
    IntegerIterator allStatesIterator;
    
    // iterator cez vsetky mozne delta-funkcie
    TransitionsIterator allTransitionsIterator;

    HashMap<Character,Matrix> currentTransitions;
    HashSet<Identificator> currentFinalStatesIds;
    Identificator currentInitialStateId;
    // pocet stavov, ktory uvazujeme
    Integer numberOfStates;
    boolean hasNext;
    
    
    public void printState(FastPrint out) throws IOException{
        out.println("#allSubsetsIterator state");
        out.println("#allTransitionsIterator state");
        out.println("#alphabet size");
        out.println("#charcter of alphabet followed by numeric representation of incidence matrix");
        
        out.println(Long.valueOf(this.allSubsetsIterator.state).toString());
        out.println(Long.valueOf(this.allTransitionsIterator.state).toString());
        out.println(Integer.valueOf(Variables.alphabet.size()).toString());
        for(Character c : Variables.alphabet){
            out.println(c.toString());
            out.println(this.allTransitionsIterator.currentMatrices.get(c).getNumericRepresentation().toString());
        }
        
        out.println("#finish of Automaton iterator backup");
    }
    
    // n je pocet stavov NKA ktore chceme
    public AutomatonIterator(int n) throws Exception{
        if (n == 0) {
            this.numberOfStates = 0;
            return;
        }
        this.hasNext = true;
        this.numberOfStates = n;
        
        this.allTransitionsIterator = new TransitionsIterator(n);
        this.currentTransitions = allTransitionsIterator.next();
        
        this.allSubsetsIterator = new SubsetIterator(n);
        this.currentFinalStatesIds = this.allSubsetsIterator.next();
        
        // staci uvazovat len stav 0 ako pociatocny, ostatne pripady su len nejake izomorfne k tomu
        this.allStatesIterator = new IntegerIterator(1);
        this.currentInitialStateId = this.allStatesIterator.next();
    }
    
    /* spravime krok v iteratore, ale bez navratovej hodnoty */
    public void skip(){
        if (!this.hasNext()) return;
        
        this.checkNext();
        
        if (!this.allStatesIterator.hasNext()){
            this.allStatesIterator = new IntegerIterator(1);
            this.allStatesIterator.skip();
            
            if(!this.allSubsetsIterator.hasNext()){
                this.allSubsetsIterator = new SubsetIterator(this.numberOfStates);
                this.currentFinalStatesIds = this.allSubsetsIterator.next();
                this.currentTransitions = this.allTransitionsIterator.next();
            }
            else{
                this.currentFinalStatesIds = this.allSubsetsIterator.next();
            }
        }
        else{
            this.currentInitialStateId = this.allStatesIterator.next();
        }
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
                this.allSubsetsIterator.reset();// = new SubsetIterator(this.numberOfStates);
                this.currentFinalStatesIds = this.allSubsetsIterator.next();
                this.currentTransitions = this.allTransitionsIterator.next();
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
        if ((this.allStatesIterator.hasNext()) || (this.allSubsetsIterator.hasNext()) || (this.allTransitionsIterator.hasNext())){
            hasNext = true;
            return hasNext;
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
        
        return new Automaton(transitions,new Identificator(0),subsetIt.random());
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
