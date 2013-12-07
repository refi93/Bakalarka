/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raf
 */
public class AutomatonAnalyzer extends Thread {
    MinimalAutomatonHashMap minimalNFAs;
    AutomatonIterator it;
    int id, numberOfWorkers;
    
    public AutomatonAnalyzer(int numberOfStates, int numberOfWorkers, int id) throws Exception{
        this.minimalNFAs = new MinimalAutomatonHashMap();
        this.it = new AutomatonIterator(numberOfStates);
        this.id = id;
        this.numberOfWorkers = numberOfWorkers;
    }
    
    @Override
    public void run(){
        int counter = 0;
        while(it.hasNext){
            if(counter % numberOfWorkers == id){
                Automaton a = it.next();
                try {
                    minimalNFAs.tryToInsert(a);
                } catch (Exception ex) {
                    Logger.getLogger(AutomatonAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                it.skip();
            }
            counter++;
        }
    }
}
