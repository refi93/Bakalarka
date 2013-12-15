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
        this.numberOfWorkers = Variables.numberOfCores;
        this.lastBackupTime = (int)((System.nanoTime() - Variables.start) / 1000000000);
    }
    
    
    public void backup() throws IOException, Exception{
        System.err.printf("started backup of thread with id%d at time %s%n",this.id, Functions.getFormattedTime(this.lastBackupTime));
        FastPrint out = new FastPrint(Integer.valueOf(id).toString());
        it.printState(out);
        this.minimalNFAsResult.print(out, lastBackupedAutomatonId);
        this.lastBackupTime = (int)((System.nanoTime() - Variables.start) / 1000000000);
        this.lastBackupedAutomatonId = this.minimalNFAsResult.allMinNFAs.size();
        out.close();
        System.err.printf("done backup at time %s%n", Functions.getFormattedTime(this.lastBackupTime));
    }
    
    
    @Override
    public void run(){
        int counter = 0;
        
        while(it.hasNext()){
            if(counter % numberOfWorkers == id){
                int currentTime = (int)((System.nanoTime() - Variables.start) / 1000000000);
                
                if (currentTime - lastBackupTime > Variables.backupInterval){
                    try {
                        this.backup();
                    } catch (Exception ex) {
                        Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
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
                        if(!minimalNFAsResult.containsEquivalent(a)){
                            minimalNFAsResult.forceInsert(a);
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
    }
}
