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
    MinimalAutomatonHashMap minimalNFAsResult;
    AutomatonIterator it;
    int id, numberOfWorkers;
    
    
    /* numberOfStates - pocet stavov v automatoch, ktore sa budu generovat
       id - na zaklade idcka thread vie, ktore automaty sa maju ratat - idcko
        udava modulo, pri ktorom sa zarata automat - pozri run()
    */
    public AutomatonAnalyzerThread(int numberOfStates, int id) throws Exception{
        this.minimalNFAsResult = new MinimalAutomatonHashMap();
        this.it = new AutomatonIterator(numberOfStates);
        this.id = id;
        this.numberOfWorkers = Variables.numberOfCores;
    }
    
    
    @Override
    public void run(){
        int counter = 0;
        MinimalAutomatonHashMap minimalNFAsCurrent = new MinimalAutomatonHashMap();
        
        while(it.hasNext()){
            if(counter % numberOfWorkers == id){
                Automaton a = it.next();
                // vypisovanie pocitadla po 100 000 nextoch
                // zalockujeme counter, aby ho ostatne thready nemohli menit
                synchronized(Variables.counterOfTestedAutomata){
                    if (Variables.counterOfTestedAutomata++ % 100000 == 0) {
                        int seconds = (int)((System.nanoTime() - Variables.start) / 1000000000);
                        System.err.printf("%d automata generated, time: %s %n", Variables.counterOfTestedAutomata - 1, Functions.getFormattedTime(seconds));
                    }
                }
                try {
                    if (!Variables.allMinimalNFAs.containsEquivalent(a)){
                        if(!minimalNFAsCurrent.containsEquivalent(a)){
                            minimalNFAsCurrent.forceInsert(a);
                            // ak presiahneme velkost hashMapy, tak ju vysypeme do vysledkov a zacneme plnit odznova
                            if (minimalNFAsCurrent.size() > Variables.hashMapSizeThreshold){
                                minimalNFAsResult = MergeThread.merge(minimalNFAsCurrent, minimalNFAsResult);
                                minimalNFAsCurrent = new MinimalAutomatonHashMap();
                            }
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                it.skip(); // vykona sa iteracia naprazdno - len sa posunie counter
            }
            counter++;
        }
        
        
        // nakoniec do vysledku pripojime doterajsie vypocty
        try {
            minimalNFAsResult = MergeThread.merge(minimalNFAsResult, minimalNFAsCurrent);
        } catch (InterruptedException ex) {
            Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
