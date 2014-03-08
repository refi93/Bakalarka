/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author raf
 */
public class Experiments {
    
    
    static int sumCollisions(HashMap<BigInteger,Integer> m){
        Integer sum = 0;
        for(BigInteger x : m.keySet()){
            sum += m.get(x);
        }
        return sum;
    }
    
    
    static double avgCollisions(HashMap<BigInteger,Integer> m){
        return ((double)sumCollisions(m) / (double)m.size());
    }
    
    
    /* tento experiment ma za ciel zistit, slova do akej dlzky treba skumat, aby sme rozlisili
       dva NKA, ci su alebo nie su ekvivalentne
    */
    public static void safeWordLengthExperiment(int n) throws Exception{
        
        int wordLength = 0;        
        
        HashMap<BigInteger, Integer> collisionCount = new HashMap<>();
        do{
            collisionCount = new HashMap<>();
            
            Variables.initializeWordToNumberMap(wordLength); // inicializujeme mapu, ktora prevadza slova na indexy v hashi
        
            MinimalAutomatonHashMap languages = new MinimalAutomatonHashMap();
            // iteraujeme cez mozne pocty stavov
            for(int i = 1;i <= n;i++){
                AutomatonIterator it = new AutomatonIterator(i);
                // budeme iterovat cez vsetky automaty s n stavmi
                while(it.hasNext()){
                    Automaton a = it.next();
                    boolean isNew = languages.tryToInsert(a);
                    // ak je tento automat reprezentant noveho jazyka, tak zistime, ci jeho hash na zaklade slov koliduje s niektorym predoslym
                    if (isNew){
                        BigInteger hashCode = a.hashCodeFromWords(wordLength);
                        if (collisionCount.containsKey(hashCode)){
                            Integer oldVal = collisionCount.get(hashCode);
                            collisionCount.put(hashCode, oldVal + 1);
                        }
                        else{
                            collisionCount.put(hashCode, 0);
                        }
                    }
                }
            }
            
            System.out.println(wordLength + " & " + sumCollisions(collisionCount));
            wordLength++;
        }while(sumCollisions(collisionCount) > 0);
    }
    
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
    
    
    /* vygeneruje vsetky NFA do danej velkosti a overi ich minimalnost 
        tuto metodu treba spustit pred kazdym inym experimentom, kedze sa opieraju
        o vysledky z tejto metody
    */
    public static void generateAllNFAsOfSize(int limit) throws IOException, Exception{
        FastPrint.cleanFile(Variables.outputFileForAutomata); // precistime subory s automatmi z predosleho behu
        FastPrint.cleanFile(Variables.outputFile);
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

            Variables.allMinimalDFAs = MergeThread.merge(Variables.allMinimalDFAs,result);
            
            System.err.printf("%d languages found%n", Variables.allMinimalDFAs.size());
        }
        
        System.err.printf("%d automata tested%n",Variables.counterOfTestedAutomata);
        System.err.printf("Generating automata ended at time: %s%n",Functions.getFormattedTime((int)((System.nanoTime() - Variables.start) / 1000000000)));
        
        Variables.outputStream.close(); // zavrieme hlavny output stream
    }
    
    
    /*
        Ciel je previest subor automata.txt s explicitne vypisanymi automatmi na zoznam hashov 
        a naplnime globalny zoznam jazykov, resp. ich kodov tymi nacitanymi
        pre format vstupu pozri subor automata.txt
    */
    public static void automataFileToHashes() throws FileNotFoundException, Exception{
        PrintWriter writer = new PrintWriter("automataHashes.txt", "UTF-8");
        
        Scanner s = new Scanner(new File(Variables.automataFile));
        HashSet<Tuple> automataHashCodes = new HashSet<>();
        int counter  = 0;
        while(s.hasNext()){
            if (counter++ % 100000 == 99999) System.err.println(counter + " languages processed");;
            Automaton.readAutomaton(s); // NKA nas nezaujima
            Automaton a = Automaton.readAutomaton(s);
            if (a != null){
                if(automataHashCodes.contains(a.myHashCode())) {
                    System.err.println("FAIL");
                    System.exit(1);
                }
                //automataHashCodes.add(a.myHashCode());
                writer.println(a.myHashCode());
            }
        }
        // naplnime globalny zoznam jazykov, resp. ich kodov tymi nacitanymi
        Variables.allMinimalDFAs.allMinDFACodes = automataHashCodes;
        
        writer.close();
    }
    
    
    /* nacitanie hashov zo suboru - predpokladame, ze v kazdom riadku je 
        jeden BigInteger kodujuci matice prechodovych funkcii a nasledne Integer 
        kodujuci mnozinu konecnych stavov
    */
    public static void readAutomataHashes() throws IOException{
        BufferedReader bi = new BufferedReader(new InputStreamReader(new FileInputStream(Variables.hashesFile)));
        int counter = 0;
        while (bi.ready()){
            String str = bi.readLine();
            String[] splited = str.split("\\s+");
            
            BigInteger first = new BigInteger(splited[0]);
            Integer second = Integer.parseInt(splited[1]);
            
            //System.out.println(first + " " + second + " " + third);
            Tuple hash = new Tuple(first, second);
            //if (counter > 4000000)
                Variables.allMinimalDFAs.insertValue(hash);
            if (counter++ % 100000 == 99999){
                System.err.println(counter + " hashes processed");
            }
        }
    }
    
    
    /* nacita zo suboru automaty do 4 stavov (predpoklada sa, ze tam su) a zacne generovat
    nahodne NKA 5-stavove
    */
    public static void fiveStateNFAs(long numberOfSamples) throws Exception{
        Variables.initialize();
        Variables.allMinimalDFAs.clear(); // precistime mapu s automatmi
        Experiments.readAutomataHashes(); // nacitame kody jazykov akceptovanych 4 a menej-stavovymi NKA
        AutomatonIterator it = new AutomatonIterator(5);
        long counter = 0;
        int time = (int)((System.nanoTime()) / 1000000000);

        FastPrint fp = new FastPrint("5StateNFAvsDFA" + time + ".txt", false); // subor, kam sa vypisu rozdiely v poctoch stavov min NFA, DFA
        FastPrint automata = new FastPrint("5StateAutomata" + time + ".txt", false); // subor, kam sa vypisu samotne najdene NFA, DFA
        automata.println(
                    "#initial state is fixed to 0 the format of output is the following\n"
                            + "#/number of the automaton\n"
                            + "#number of states\n"
                            + "#id of initial state (-1 if none)\n"
                            + "#number of final states followed by final states enumeration\n"
                            + "#number of transitions followed by its enumeration\n"
                            + "#from_state to_state character\n"
                            + "#begin of output:\n"
            );
        
        for(long i = 0;i < numberOfSamples;i++){
            
            if (i % 100000 == 0) {
                int seconds = (int)((System.nanoTime() - Variables.start) / 1000000000);
                System.err.printf("%d automata generated, time: %s, %d automata in MinimalAutomatonHashMap%n", i, Functions.getFormattedTime(seconds),Variables.allMinimalDFAs.size());
            }
            
            Automaton a = it.random();
            if(Variables.allMinimalDFAs.tryToInsert(a)){
                counter++;
                fp.println(a.numberOfStates() + " " + a.minimalDFA().numberOfStates());
                a.print(automata, counter);
            }
        }
        fp.close();
    }
    
    public static void automataDistributionExperiment(int n) throws Exception{
        MinimalAutomatonHashMapWithCounter languages = new MinimalAutomatonHashMapWithCounter();
        ArrayList<Tuple> InterestingLanguageHashCodes = new ArrayList<>(); // tu si zapamatame kody jazykov, ktore chceme analyzovat - 
        // teda tie s nedeterministickou zlozitostou n
        
        for(int i = 1;i <= n;i++){
            NaiveAutomatonIterator it = new NaiveAutomatonIterator(i);
            int j = 0;
            while(it.hasNext()){
                j++;
                Automaton a = it.next();
                if (languages.tryToInsert(a) && (i == n)){ // zaujimaju nas len n-stavove NKA
                    InterestingLanguageHashCodes.add(a.myHashCode());
                }
            }
            System.out.println(j);
        }
        
        ArrayList<Integer> x = new ArrayList(); // tu si pamatame pocty
        FastPrint fp = new FastPrint("AutomataDistribution.txt",false);
        for(Tuple t : InterestingLanguageHashCodes){
            x.add(languages.allMinDFACodesWithCounter.get(t));
            fp.println(t + " " + languages.allMinDFACodesWithCounter.get(t));
        }
        fp.close();
        Collections.sort(x);
        fp = new FastPrint("sortedDistribution.txt",false);
        for(Integer a : x){
            fp.println(a.toString());
        }
        fp.close();
    }
}