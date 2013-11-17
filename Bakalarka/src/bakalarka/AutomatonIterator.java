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
public class AutomatonIterator {
    
    // tu sa budu postupne generovat vsetky mozne matice prechodov
    HashMap<Character, MatrixIterator> transitions;
    // tu sa budu postupne generovat vsetky mozne mnoziny akceptacnych stavov
    SubsetIterator allSubsets;
    // id aktualneho pociatocneho stavu
    Integer currentInitialState;
    // pocet stavov, ktory uvazujeme
    Integer numberOfStates;
    
    
    public AutomatonIterator(int n) throws Exception{
        this.transitions = new HashMap<>();
        for(Character c : Variables.alphabet){
            this.transitions.put(c, new MatrixIterator(n));
        }
        this.allSubsets = new SubsetIterator(n);
        this.currentInitialState = 0;
        this.numberOfStates = n;
    }
    
    
    /* vrati nasledujuci automat s danym poctom stavov v poradi */
    public Automaton next() throws Exception{
        if (!this.hasNext()) throw new Exception("Iterator overflow exception");
        HashMap<Character,Matrix> retTransitions = this.materializeTransitions();
        Identificator retInitialStateId = new IntegerIdentificator(this.currentInitialState);
        HashSet<Identificator> retFinalStatesIds = this.allSubsets.current();
        
        if (this.currentInitialState >= this.numberOfStates - 1){
            this.currentInitialState = 0;
            if (!this.allSubsets.hasNext()){
                this.allSubsets = new SubsetIterator(this.numberOfStates);
                for (Character c : Variables.alphabet){
                    if (!this.transitions.get(c).hasNext()){
                        this.transitions.put(c, new MatrixIterator(this.numberOfStates));
                    }
                    else{
                        this.transitions.get(c).next();
                        break;
                    }
                }
            }
            else{
                this.allSubsets.next();
            }
        }
        else{
            this.currentInitialState++;
        }
        return new Automaton(retTransitions,retInitialStateId,retFinalStatesIds);
    }
    
    
    public boolean hasNext() throws Exception{
        if ((this.currentInitialState < this.numberOfStates - 1) || (this.allSubsets.hasNext())){
            return true;
        }
        for(Character c : Variables.alphabet){
            if (this.transitions.get(c).hasNext()) return true;
        }
        return false;
    }
    
    
    public HashMap<Character, Matrix> materializeTransitions() throws Exception{
        HashMap<Character, Matrix> ret = new HashMap<>();
        for(Character c : Variables.alphabet){
            ret.put(c, this.transitions.get(c).current());
        }
        return ret;
    }
}
