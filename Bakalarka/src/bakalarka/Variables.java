/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.Arrays;
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
}
