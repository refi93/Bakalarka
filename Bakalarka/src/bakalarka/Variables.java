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
    
    /* abeceda */
    static ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('0','1'));
    
    static boolean allowTrashState = true; // ci sa maju povolit odpadove stavy pri determinizacii 
    static boolean disableTrashState = false;
    
    static Random generator = new Random(); // zdroj nahody
    static String outputFile = "./out.txt"; // kam sa posiela vystup z programu
    static HashMap<String, Long> WordToNumberMap; //mapa, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat do bitSetu
    
    
    static void initializeWordToNumberMap() throws Exception{
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
