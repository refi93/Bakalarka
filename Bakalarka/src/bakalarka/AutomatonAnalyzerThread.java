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
    
    
    /*public void backup() throws IOException, Exception{
        System.err.printf("started backup of thread with id%d at time %s%n",this.id, Functions.getCurrentTime());
        FastPrint out = new FastPrint(Integer.valueOf(id).toString());
        it.printState(out);
        this.minimalNFAsResult.print(out, lastBackupedAutomatonId);
        this.lastBackupTime = (int)((System.nanoTime() - Variables.start) / 1000000000);
        this.lastBackupedAutomatonId = this.minimalNFAsResult.allMinNFAs.size();
        out.close();
        System.err.printf("done backup at time %s%n", Functions.getFormattedTime(this.lastBackupTime));
    }*/
    
    
    @Override
    public void run(){
        long counter = 0;
        
        // inicializujeme vypis automatov
        FastPrint vypisAutomatov = null;
        long automatonCounter = (long)0;
        try {
            vypisAutomatov = new FastPrint("./automata.txt");
            
            vypisAutomatov.println(
                    "#initial state is fixed to 0 the format of output is the following\n"
                            + "#/number of the automaton\n"
                            + "#number of states\n"
                            + "#id of initial state (-1 if none)\n"
                            + "#number of final states followed by final states enumeration\n"
                            + "#number of transitions followed by its enumeration\n"
                            + "#from_state to_state character\n"
                            + "#|minNFA number of states vs minDFA number of states|\n"
                            + "#begin of output:\n"
            );
            
        } catch (IOException ex) {
            Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        while(it.hasNext()){
            if(counter % numberOfWorkers == id){
                int currentTime = Functions.getCurrentTime();
                
                //if ((Variables.backupInterval != -1)&&(currentTime - lastBackupTime > Variables.backupInterval)){
                /*if ((this.minimalNFAsResult.size() % 1000 == 0)&&(this.numberOfStates == 4)){
                    try {
                        this.backup();
                    } catch (Exception ex) {
                        Logger.getLogger(AutomatonAnalyzerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }*/
                
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
                    if (!Variables.allMinimalNFAs.containsEquivalent(a)){ // prvy test je vyskusat, ci to neni ekvivalentne s niektorym s mensich automatov
                        if(minimalNFAsResult.tryToInsert(a)){
                            automatonCounter++;
                            //vypisAutomatov.println("/" + Long.valueOf(automatonCounter).toString());
                            a.print(vypisAutomatov,automatonCounter);
                            //vypisAutomatov.println("#" + Long.valueOf(automatonCounter).toString());
                            //vypisAutomatov.println(a.toString());
                            Automaton switchedA = a.switchLetters();
                            if(minimalNFAsResult.tryToInsert(switchedA)){
                                automatonCounter++;
                                //vypisAutomatov.println("/" + Long.valueOf(automatonCounter).toString(),automatonCounter);
                                switchedA.print(vypisAutomatov,automatonCounter);
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
        
        
    }
}
