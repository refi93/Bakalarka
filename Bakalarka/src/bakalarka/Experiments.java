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
public class Experiments {
    
    
    /* tato metoda ma za ulohu postupne stromovo spojit zoznamy kandidatov na minimalne NKA */
    public static MinimalAutomatonHashMap treeMerge(ArrayList<MinimalAutomatonHashMap> listOfCandidates) throws InterruptedException{
        if(listOfCandidates.isEmpty()){
            return null;
        }
        
        while(listOfCandidates.size() != 1){
            System.err.printf("Number of candidates lists: %d%n", listOfCandidates.size());
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
       boolean computeMinimalDFA - ci sa ma vyratat a vypisat minimalne DFA
    */
    /*public static void printAutomaton(Automaton a, long counter, FastPrint out, boolean computeMinimalDFA) throws IOException, Exception{
        out.println("#" + counter);
        a.print(out);
        if (computeMinimalDFA){
            Automaton detA = a.minimalDFA();
            detA.print(out);
            out.println("|" + a.getNumberOfStates() + " vs " + detA.getNumberOfStates() + "|\n");
            if (Variables.allMinimalNFAs.allMinDFAs.size() % 10000 == 0){
                System.err.printf("%d minimal NFAs proceeded%n",Variables.allMinimalNFAs.allMinDFAs.size());
            }
        }
    }*/
    
    
    /* vygeneruje vsetky NFA do danej velkosti a overi ich minimalnost 
        tuto metodu treba spustit pred kazdym inym experimentom, kedze sa opieraju
        o vysledky z tejto metody
    */
    public static void GenerateAllNFAsOfSize(int limit) throws IOException, Exception{
        Variables.initialize(); // nainizializovanie mapy a niektorych premennych, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat     
        
        for (int i = 1; i <= limit; i++) {
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

            
            // pozbierame pracu od slaveov a pospajame vysledky dokopy
            ArrayList<MinimalAutomatonHashMap> candidates = new ArrayList<>();
            for(AutomatonAnalyzerThread slave : slaves) {
                candidates.add(slave.minimalNFAsResult);
            }
            MinimalAutomatonHashMap result = treeMerge(candidates);
            
            System.err.printf("Merging ended, time: %s%n",Functions.getFormattedTime((int)((System.nanoTime() - Variables.start) / 1000000000)));

            Variables.allMinimalNFAs = MergeThread.merge(Variables.allMinimalNFAs,result);
            
            System.err.printf("%d languages found%n", Variables.allMinimalNFAs.size());
        }
        
        System.err.printf("%d automata tested%n",Variables.counterOfTestedAutomata);
        System.err.printf("Generating automata ended at time: %s%n",Functions.getFormattedTime((int)((System.nanoTime() - Variables.start) / 1000000000)));
        
        Variables.outputStream.close(); // zavrieme hlavny output stream
    }
    
    
}