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
    public static void printAutomaton(Automaton a, long counter, FastPrint out, boolean computeMinimalDFA) throws IOException, Exception{
        out.println("#" + counter);
        a.print(out);
        if (computeMinimalDFA){
            Automaton detA = a.minimalDFA();
            detA.print(out);
            out.println("|" + a.getNumberOfStates() + " vs " + detA.getNumberOfStates() + "|\n");
            if (Variables.allMinimalNFAs.allMinNFAs.size() % 10000 == 0){
                System.err.printf("%d minimal NFAs proceeded%n",Variables.allMinimalNFAs.allMinNFAs.size());
            }
        }
    }
    
    
    /* vygeneruje vsetky NFA do danej velkosti a overi ich minimalnost 
        tuto metodu treba spustit pred kazdym inym experimentom, kedze sa opieraju
        o vysledky z tejto metody
    */
    public static void GenerateAllNFAsOfSize(int limit) throws IOException, Exception{
        Variables.initialize(); // nainizializovanie mapy a niektorych premennych, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat
        FastPrint out = new FastPrint();        
        
        // vypis do suboru, aby bol jasny format
        out.println(
                "#initial state is fixed to 0 the format of output is the following\n"
                + "#/number of the automaton\n"
                + "#number of states\n"
                + "#id of initial state (-1 if none)"
                + "#number of final states followed by final states enumeration\n"
                + "#number of transitions followed by its enumeration\n"
                + "#from_state to_state character\n"
                + "#|minNFA number of states vs minDFA number of states|\n"
                + "#begin of output:"
                );
        
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

            // najprv vlozime tie automaty, o ktorych mame istotu, ze su minimalne
            for(Automaton a : result.allMinNFAs){
                Variables.allMinimalNFAs.forceInsert(a);
                printAutomaton(a,Variables.allMinimalNFAs.allMinNFAs.size(),out,true);
            }
            // teraz ideme hadzat tie, co maju prehodenu delta-funckiu - tie su tiez potencialne minimalne
            if (Variables.alphabet.size() == 2){
                for (Automaton a : result.allMinNFAs) {
                    // optimalizacia urcena pre 2-pismenkovu abecedu
                    // teraz prehodime prechody na 0 a 1 - tento automat je potencialne tiez minimalny
                    Automaton switchedOne = a.switchLetters();
                    boolean isNew = Variables.allMinimalNFAs.tryToInsert(switchedOne);
                    if (isNew) {
                        printAutomaton(switchedOne, Variables.allMinimalNFAs.allMinNFAs.size(), out,true);
                    }
                }
            }
            System.err.printf("%d languages found%n", Variables.allMinimalNFAs.allMinNFAs.size());
        }
        
        out.println(new Integer(Variables.allMinimalNFAs.allMinNFAs.size()).toString());
        System.err.printf("%d automata tested%n",Variables.counterOfTestedAutomata);
        System.err.printf("Generating automata ended at time: %s%n",Functions.getFormattedTime((int)((System.nanoTime() - Variables.start) / 1000000000)));
        out.close();
    }
    
    
    /* vrati pocet jedinecnych jazykov do daneho poctu stavov NFA */
    public static long UniqueLanguages(int limit){
        return Variables.allMinimalNFAs.allMinNFAs.size();
    }
    
    
    /* tento experiment vyprodukuje tabulku s porovnanim, kolko stavov treba na
    NFA a kolko na ekvivalentny DFA
    */
    public static long[][] NfaDfaTable(int limit) throws Exception{
        int maxDFAStates = 1 << (limit + 1);
        long ret[][] = new long[limit][maxDFAStates];
        for(int i = 0;i < limit;i++){
            for(int j = 0;j < maxDFAStates;j++){
                ret[i][j] = 0;
            }
        }
        
        for(Automaton a : Variables.allMinimalNFAs.allMinNFAs){
            ret[a.getNumberOfStates()][a.minimalDFA().getNumberOfStates()]++;
        }
        
        return ret;
    }
    
    
    /* tento experiment ma za ciel zistit "bezpecnu dlzku" slova pre dany pocet stavov, 
    t.j. dlzku slova m taku, ze pre lubovolne 2 NFA s n stavmi plati, ze su
    ekvivalentne prave vtedy, ked sa rovnaju mnoziny vsetkych slov do dlzky m,
    ltoru generuju
    */
    public static int SafeWordLengthExperiment(int limit){
        //TODO
        return 0;
    }
    
}