/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

/**
 *
 * @author raf
 */
public class NaiveAutomatonIterator extends AutomatonIterator{

    public NaiveAutomatonIterator(int n) throws Exception {
        super(n);
        this.allSubsetsIterator = new NaiveSubsetIterator(n);
        this.allTransitionsIterator = new NaiveTransitionsIterator(n);
        this.currentTransitions = this.allTransitionsIterator.next();
        this.currentFinalStatesIds = this.allSubsetsIterator.next();
    }
    
}
