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
    static HashMap<BinaryWord, Integer> WordToNumberMap; //mapa, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat do bitSetu
    
    static MinimalAutomatonHashMap allMinimalNFAs = new MinimalAutomatonHashMap(); // tu si pamatame vsetky mensie doteraz ziskane NFA, resp. min. DFA k nim
    
    static long start = 0;
    
    static Long counterOfTestedAutomata = (long)0; // pocitadlo vygenerovanych automatov
    
    
    public static String hashString2 = "1010011001000111010101000011101";
    public static String hashString1 = "0101100110111000101010111100010";
    
    
    public static int hashWordLength = 5; // dlzka slova, po ktoru hashujeme 
    
    public static int backupInterval = 21600; // backup vysledkov threadu po kazdych 6 hodinach
    
    // sluzi na inicializaciu niektorych premennych - mapy so slovami, casu, ...
    static void initialize() throws Exception{
        start = System.nanoTime(); // nastavime cas startu programu
        WordToNumberMap = new HashMap<>();
        // toto ma zmysel len pre 2-pismenkovu abecedu
        if(Variables.alphabet.size() == 2){
            Automaton a = new Automaton();
            // spravime si automat pre Sigma*
            a.addState(0);
            a.setInitialStateId(0);
            a.addFinalState(0);
            a.addTransition(0, 0, Variables.alphabet.get(0));
            a.addTransition(0, 0, Variables.alphabet.get(1));
            HashSet<BinaryWord> words = a.allWordsOfLength(Variables.hashWordLength);
            // slova ulozime do mapy a priradime k nim cislo
            int counter = 0;
            for(BinaryWord word : words){
                WordToNumberMap.put(word, counter);
                counter++;
            }
        }
    }
    
    public static Long connectedCount = (long)0;
    
}
