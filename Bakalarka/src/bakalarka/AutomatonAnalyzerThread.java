/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.IOException;
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
    int numberOfStates = 0;
    int id, numberOfWorkers;
    int lastBackupTime = 0; // posledny cas zalohovania vysledkov threadu
    int lastBackupedAutomatonId = 0; // id posledneho odzalohovaneho automatu
    
    
    /* numberOfStates - pocet stavov v automatoch, ktore sa budu generovat
       id - na zaklade idcka thread vie, ktore automaty sa maju ratat - idcko
        udava modulo, pri ktorom sa zarata automat - pozri run()
    */
    public AutomatonAnalyzerThread(int numberOfStates, int id) throws Exception{
        this.minimalNFAsResult = new MinimalAutomatonHashMap();
        this.it = new AutomatonIterator(numberOfStates);
        this.id = id;
        this.numberOfStates = numberOfStates;
        this.numberOfWorkers = Variables.numberOfCores;
        this.lastBackupTime = (int)((System.nanoTime() - Variables.start) / 1000000000);
    }
    
    
    @Override
    public void run(){
        long counter = 0;
        
        // inicializujeme vypis automatov
        FastPrint vypisAutomatov = null;
        long automatonCounter = (long)0;
        try {
            vypisAutomatov = new FastPrint(Variables.outputFileForAutomata);
            
            vypisAutomatov.println(
                    "#initial state is fixed to 0 the format of output is the following\n"
                            + "#/number of the automaton\n"
                            + "#number of states\n"
                            + "#id of initial state (-1 if none)\n"
                            + "#number of final states followed by final states enumeration\n"
                            + "#number of transitions followed by its enumeration\n"
                            + "#from_state to_state character\n"
                            + "#begin of output:\n"
            );
            
        } catch (IOException ex) {
            Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        while(it.hasNext()){
            if(counter % numberOfWorkers == id){
                int currentTime = Functions.getCurrentTime();
                
                
                Automaton a = it.next();
                // vypisovanie pocitadla po 100 000 nextoch
                // zalockujeme counter, aby ho ostatne thready nemohli menit
                synchronized(Variables.counterOfTestedAutomata){
                    if (Variables.counterOfTestedAutomata++ % 100000 == 0) {
                        int seconds = (int)((System.nanoTime() - Variables.start) / 1000000000);
                        System.err.printf("Thread id %d: %d automata generated, time: %s, %d automata in ResultMinimalAutomatonHashMap%n", this.id, Variables.counterOfTestedAutomata - 1, Functions.getFormattedTime(seconds), this.minimalNFAsResult.size());
                    }
                }
                try {
                    if (!Variables.allMinimalDFAs.containsEquivalent(a)){ // prvy test je vyskusat, ci to neni ekvivalentne s niektorym s mensich automatov
                        if(minimalNFAsResult.tryToInsert(a)){
                            automatonCounter++;
                            a.print(vypisAutomatov,automatonCounter+Variables.allMinimalDFAs.size());
                            
                            // nasledujuca optimalizacia je implementovana len pre autoamty nad 2-znakovou abecedou
                            if (Variables.alphabet.size() == 2){
                                Automaton switchedA = a.switchLetters();
                                Variables.counterOfTestedAutomata++;
                                if(minimalNFAsResult.tryToInsert(switchedA)){ 
                                    automatonCounter++;
                                    switchedA.print(vypisAutomatov,automatonCounter+Variables.allMinimalDFAs.size());
                                }
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
        
        
        // zavrieme output automatov aby sa vsetko vypisalo
        try {
            vypisAutomatov.close(); 
        } catch (IOException ex) {
            Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
