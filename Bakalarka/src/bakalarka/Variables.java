/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author raf
 * trieda obsahujuca potrebne globalne premenne
 */
public class Variables {
    
    static int numberOfCores = 1;//Runtime.getRuntime().availableProcessors(); // pocet jadier cpu
    
    static String automataFile = "automata.txt";
    static String hashesFile = "automataHashes.txt";
    
    /* abeceda */
    static ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('0','1'));
    
    
    // toto su len konstanty, aby nebolo vo funkciach true a false, ale uvedeny vyznam
    static boolean allowTrashState = true; // ci sa maju povolit odpadove stavy pri determinizacii 
    static boolean disableTrashState = false;
    
    
    static Random generator = new Random(); // zdroj nahody
    public static String outputFile = "./out.txt"; // kam sa posiela vystup z programu
    public static String outputFileForAutomata = "./automata.txt";
    public static FastPrint outputStream; // kam vypisujeme dvojice - pocet stavov minNFA vs minDFA
    
    static MinimalAutomatonHashMap allMinimalNFAs = new MinimalAutomatonHashMap(); // tu si pamatame vsetky mensie doteraz ziskane NFA, resp. min. DFA k nim
    
    static long start = 0;
    
    static Long counterOfTestedAutomata = (long)0; // pocitadlo vygenerovanych automatov
        
    
    public static HashMap<String, Integer> wordToNumberMap; //mapa, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat do bitSetu
    
    // sluzi na inicializaciu niektorych premennych - casu, ...
    static void initialize() throws Exception{
        start = System.nanoTime(); // nastavime cas startu programu
        outputStream = new FastPrint();
        FastPrint.cleanFile(outputFileForAutomata);
        FastPrint.cleanFile(outputFile);
        
    }
    
    
    // tuto nastavime mapu zo slov na inty (vyuziva sa pri experimente s dlzkou slova ktore rozlisi dva NFA)
    public static void initializeWordToNumberMap(int wordLength) throws Exception{
        wordToNumberMap = new HashMap<>();
        Automaton a = new Automaton();
        // spravime si automat pre Sigma*
        a.addState(0);
        a.setInitialStateId(0);
        a.addFinalState(0);
        for(Character c : Variables.alphabet){
            a.addTransition(0, 0, c);
        }
        HashSet<String> words = a.allWordsOfLength(wordLength);
        // slova ulozime do mapy a priradime k nim cislo
        int counter = 0;
        for(String word : words){
            wordToNumberMap.put(word, counter);
            counter++;
        }
    }
    
    
    public static Long connectedCount = (long)0;
    
}
