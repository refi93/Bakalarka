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
    
    
    public static int hashWordLength = 5; // dlzka slova, po ktoru hashujeme 
    
    public static int backupInterval = -1; // backup interval zadany v sekundach (-1 ak je backup vypnuty)
    
    
    // sluzi na inicializaciu niektorych premennych - mapy so slovami, casu, ...
    static void initialize() throws Exception{
        start = System.nanoTime(); // nastavime cas startu programu
        outputStream = new FastPrint();
        FastPrint.cleanFile(outputFileForAutomata);
        FastPrint.cleanFile(outputFile);
    }
    
    public static Long connectedCount = (long)0;
    
}
