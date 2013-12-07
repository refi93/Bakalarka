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
    
    static int numberOfCores = Runtime.getRuntime().availableProcessors(); // pocet jadier cpu
    
    /* abeceda */
    static ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('0','1'));
    
    static boolean allowTrashState = true; // ci sa maju povolit odpadove stavy pri determinizacii 
    static boolean disableTrashState = false;
    
    static Random generator = new Random(); // zdroj nahody
    static String outputFile = "./out.txt"; // kam sa posiela vystup z programu
    static HashMap<String, Long> WordToNumberMap; //mapa, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat do bitSetu
    
    static MinimalAutomatonHashMap allMinimalNFAs = new MinimalAutomatonHashMap(); // tu si pamatame vsetky mensie doteraz ziskane NFA, resp. min. DFA k nim
    
    static long start = 0;
    
    static Long counter = (long)0; // pocitadlo poctu vygenerovanych automatov
    
    // sluzi na inicializaciu niektorych premennych - mapy so slovami, casu, ...
    static void initialize() throws Exception{
        start = System.nanoTime(); // nastavime cas startu programu
        WordToNumberMap = new HashMap<>();
        Automaton a = new Automaton();
        a.addState(0);
        a.setInitialStateId(0);
        a.addFinalState(0);
        a.addTransition(0, 0, Variables.alphabet.get(0));
        a.addTransition(0, 0, Variables.alphabet.get(1));
        HashSet<String> words = a.allWordsOfLength(5);
        long counter = 0;
        for(String word : words){
            WordToNumberMap.put(word, counter);
            counter++;
        }
    }
    
}
