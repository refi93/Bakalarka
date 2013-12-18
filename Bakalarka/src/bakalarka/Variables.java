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
    
    
    // toto su len konstanty, aby nebolo vo funkciach true a false, ale uvedeny vyznam
    static boolean allowTrashState = true; // ci sa maju povolit odpadove stavy pri determinizacii 
    static boolean disableTrashState = false;
    
    // hranicna velkost hashMapy, ktoru je este slusne pouzivat na priebezne vypocty, kompenzujeme tym slabu hash funkciu
    public static int currentResultThresholdSize = 80000; 
    
    
    static Random generator = new Random(); // zdroj nahody
    static String outputFile = "./out.txt"; // kam sa posiela vystup z programu
    static HashMap<BinaryWord, Integer> WordToNumberMap; //mapa, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat do bitSetu
    
    static MinimalAutomatonHashMap allMinimalNFAs = new MinimalAutomatonHashMap(); // tu si pamatame vsetky mensie doteraz ziskane NFA, resp. min. DFA k nim
    
    static long start = 0;
    
    static Long counterOfTestedAutomata = (long)0; // pocitadlo vygenerovanych automatov
    
    
    public static ArrayList<String> hashStrings = new ArrayList<>();
    public static int numberOfHashStrings = 64;
    public static int maxHashStringLength = 8;
    
    
    public static int hashWordLength = 5; // dlzka slova, po ktoru hashujeme 
    
    public static int backupInterval = -1; // backup interval zadany v sekundach (-1 ak je backup vypnuty)
    
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
        
        
        /*hashStrings.add("");
        hashStrings.add("1010101");
        hashStrings.add("0101010");
        hashStrings.add("0");
        hashStrings.add("00");
        hashStrings.add("000");
        hashStrings.add("0000");
        hashStrings.add("00000");
        hashStrings.add("000000");
        hashStrings.add("1");
        hashStrings.add("11");
        hashStrings.add("111");
        hashStrings.add("1111");
        hashStrings.add("11111");
        hashStrings.add("111111");
        
        Random generator = new Random();
        int curSize = hashStrings.size();
        for(int i = 0;i < numberOfHashStrings - curSize;i++){
            BinaryWord bw = new BinaryWord();
            int limit = 7;//generator.nextInt(maxHashStringLength + 1);
            for(int j = 0;j < limit;j++){
                bw.append(generator.nextInt(2));
            }
            hashStrings.add(bw.toString());
            System.out.println(bw.toString());
        }*/
    }
    
    public static Long connectedCount = (long)0;
    
}
