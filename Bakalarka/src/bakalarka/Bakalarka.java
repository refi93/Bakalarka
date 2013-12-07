/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author raf
 */

public class Bakalarka {
    

    /* tato metoda ma za ulohu postupne stromovo spojit zoznamy kandidatov na minimalne NKA */
    public static MinimalAutomatonHashMap treeMerge(ArrayList<MinimalAutomatonHashMap> listOfCandidates) throws InterruptedException{
        if(listOfCandidates.isEmpty()){
            return null;
        }
        
        while(listOfCandidates.size() != 1){
            int pocetZoznamov = listOfCandidates.size();
            ArrayList<MergeThread> mergers = new ArrayList<>();
            
            for(int i = 0;i < (pocetZoznamov / 2);i++){
                mergers.add(new MergeThread(listOfCandidates.get(2 * i), listOfCandidates.get(2 * i + 1)));
                mergers.get(i).start(); // nastarujeme kazdy z threadov
            }
            
            for(int i = 0;i < (pocetZoznamov / 2);i++){
                mergers.get(i).join(); // pockame si, kym skonci kazdy z threadov
            }
            
            ArrayList<MinimalAutomatonHashMap> newCandidates = new ArrayList<>();
            for(int i = 0;i < (pocetZoznamov / 2);i++){
                newCandidates.add(mergers.get(i).result);
            }
            
            if(pocetZoznamov % 2 == 1){
                newCandidates.add(listOfCandidates.get(pocetZoznamov - 1));
            }
            
            listOfCandidates = newCandidates;
        }
        
        return listOfCandidates.get(0);
    }
    
    
    /* Automaton a - automat na vypisanie
       long counter - pocitadlo - poradie toho automatu
       FastPrint out - kam sa ma vypisovat ten automat
    */
    public static void printAutomaton(Automaton a, long counter, FastPrint out) throws IOException, Exception{
        out.println("/" + counter);
        a.print(out);
        Automaton detA = a.minimalDFA().normalize();
        detA.print(out);
        out.println("|" + a.getNumberOfStates() + " vs " + detA.getNumberOfStates() + "|\n");
        /*out.println("----------------");
        out.println(counter + "\nmin NFA: \n" + a);
        
        Automaton detA = a.minimalDFA().normalize();
        out.println("min DFA: \n" + detA);

        out.println(a.getNumberOfStates() + " vs " + detA.getNumberOfStates());
        out.println("----------------");*/
    }
    
    
    public static void main(String[] args) throws Exception {
        Variables.initialize(); // nainizializovanie mapy a niektorych premennych, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat
        FastPrint out = new FastPrint();
        
        MinimalAutomatonHashMap minimalNFAs = new MinimalAutomatonHashMap();
        
        long counter = 0;
        long start = System.nanoTime();
        
        // vypis do suboru, aby bol jasny format
        out.println(
                "initial state is fixed to 0 the format of output is the following\n"
                + "/number of the automaton"
                + "number of states\n"
                + "number of final states followed by final states enumeration\n"
                + "number of transitions followed by its enumeration\n"
                + "from_state to_state character\n"
                + "|minNFA number of states vs minDFA number of states|\n"
                + "begin of output:"
                );
        
        for (int i = 1; i <= 2; i++) {
            // najprv rozdelime pracu slaveom
            ArrayList<AutomatonAnalyzerThread> slaves = new ArrayList<>();
            for(int j = 0;j < Variables.numberOfCores;j++){
                slaves.add(new AutomatonAnalyzerThread(i,j));
            }
            
            // nastartujeme ich
            for(AutomatonAnalyzerThread slave : slaves){
                slave.start();
            }
            
            // pockame, kym vsetci skoncia
            for(AutomatonAnalyzerThread slave : slaves){
                slave.join();
            }

            System.err.printf("Merging candidates started for " + i + " states at time: %s%n",Functions.getFormattedTime((int)((System.nanoTime() - Variables.start) / 1000000000)));

            
            // pozbierame pracu od slaveov a pospajame dokopy
            ArrayList<MinimalAutomatonHashMap> candidates = new ArrayList<>();
            for(AutomatonAnalyzerThread slave : slaves) {
                candidates.add(slave.minimalNFAs);
            }
            MinimalAutomatonHashMap result = treeMerge(candidates);
            
            
            for (Automaton current : result.allMinNFAs) {
                boolean isNew = Variables.allMinimalNFAs.tryToInsert(current);
                if (isNew) {
                    counter++;
                    printAutomaton(current, counter, out);
                    
                    // optimalizacia spojena s vymenou 1 a 0 v delta funckii pre 2-pismenkovu abecedu
                    if (Variables.alphabet.size() == 2){
                        // teraz prehodime prechody na 0 a 1 - tento automat je potencialne tiez minimalny
                        Automaton switchedOne = current.switchLetters();
                        boolean isNewTheSwitchedOne = Variables.allMinimalNFAs.tryToInsert(switchedOne);
                        if (isNewTheSwitchedOne) {
                            counter++;
                            printAutomaton(switchedOne, counter, out);
                        }
                    }
                }
            }
            System.err.printf("%d languages found%n", Variables.allMinimalNFAs.allMinNFAs.size());
        }
        
        out.println(new Integer(Variables.allMinimalNFAs.allMinNFAs.size()).toString());
        System.err.printf("%d languages found%n", Variables.allMinimalNFAs.allMinNFAs.size());
        System.out.println(Variables.counter);
        //System.out.println("Safe length of words: " + minimalNFAs.max);
        System.err.printf("Program ended at time: %s%n",Functions.getFormattedTime((int)((System.nanoTime() - Variables.start) / 1000000000)));
        out.close();
    }
    
}