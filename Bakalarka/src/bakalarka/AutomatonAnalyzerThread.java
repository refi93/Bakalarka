/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raf
 * 
 * sluzi na vytvorenie threadu, ktory samostatne analyzuje nejaku cast automatov
 */
public class AutomatonAnalyzerThread extends Thread {
    MinimalAutomatonHashMap minimalNFAs;
    AutomatonIterator it;
    int id, numberOfWorkers;
    
    /* numberOfStates - pocet stavov v automatoch, ktore sa budu generovat
       id - na zaklade idcka thread vie, ktore automaty sa maju ratat - idcko
        udava modulo, pri ktorom sa zarata automat - pozri run()
    */
    public AutomatonAnalyzerThread(int numberOfStates, int id) throws Exception{
        this.minimalNFAs = new MinimalAutomatonHashMap();
        this.it = new AutomatonIterator(numberOfStates);
        this.id = id;
        this.numberOfWorkers = Variables.numberOfCores;
    }
    
    @Override
    public void run(){
        int counter = 0;
        while(it.hasNext()){
            if(counter % numberOfWorkers == id){
                Automaton a = it.next();
                try {
                    minimalNFAs.tryToInsert(a);
                } catch (Exception ex) {
                    Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                it.skip(); // vykona sa iteracia naprazdno - len sa posunie counter
            }
            counter++;
        }
    }
}
