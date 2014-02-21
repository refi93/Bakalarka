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
    
    
    /*
        Ciel je previest subor automata.txt s explicitne vypisanymi automatmi na zoznam hashov 
        a naplnime globalny zoznam jazykov, resp. ich kodov tymi nacitanymi
        pre format vstupu pozri subor automata.txt
    */
    public static void automataFileToHashes() throws FileNotFoundException, Exception{
        PrintWriter writer = new PrintWriter("automataHashes.txt", "UTF-8");
        
        Scanner s = new Scanner(new File(Variables.automataFile));
        HashSet<Triplet> automataHashCodes = new HashSet<>();
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
        Variables.allMinimalNFAs.allMinDFACodes = automataHashCodes;
        
        writer.close();
    }
    
    
    public static void readAutomataHashes() throws IOException{
        BufferedReader bi = new BufferedReader(new InputStreamReader(new FileInputStream(Variables.hashesFile)));
        int counter = 0;
        while (bi.ready()){
            String str = bi.readLine();
            String[] splited = str.split("\\s+");
            
            BigInteger first = new BigInteger(splited[0]);
            BigInteger second = new BigInteger(splited[1]);
            Integer third = Integer.parseInt(splited[2]);
            
            //System.out.println(first + " " + second + " " + third);
            Triplet hash = new Triplet(first, second, third);
            //if (counter > 4000000)
                Variables.allMinimalNFAs.insertValue(hash);
            if (counter++ % 100000 == 99999){
                System.err.println(counter + " hashes processed");
            }
        }
    }
    
    
    /* nacita zo suboru automaty do 4 stavov (predpoklada sa, ze tam su) a zacne generovat
    nahodne NKA 5-stavove
    */
    public static void fiveStateNFAs(long numberOfSamples) throws Exception{
        AutomatonIterator it = new AutomatonIterator(5);
        long counter = 0;
        for(long i = 0;i < numberOfSamples;i++){
            Automaton a = it.random();
            if(Variables.allMinimalNFAs.tryToInsert(a)){
                counter++;
                System.out.println(counter + " out of " + i);
            }
        }
    }
}