/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 *
 * @author raf
 */
public class Bakalarka {

    
    public static void main(String[] args) throws Exception {
        
        FastPrint out = new FastPrint();

        
        MinimalAutomatonHashMap minimalNFAs = new MinimalAutomatonHashMap();
        
        long counter = 0;
        long start = System.nanoTime();
        for(int i = 1;i <= 2;i++){
            AutomatonIterator it = new AutomatonIterator(i);
            while(it.hasNext()){
                if (counter % 100000 == 0){
                    System.err.printf("%d, time: %d s%n", counter, (System.nanoTime() - start) / 1000000000);
                }
                Automaton current = it.next();
                
                // pokusime sa vlozit automat, ak je novy, tak ho vypiseme
                boolean isNew = minimalNFAs.tryToInsert(current);
                
                if (isNew){
                    out.println("----------------");
                    out.println(counter + "\nmin NFA: \n" + current);
                    
                    Automaton detCurrent = current.minimalDFA().normalize();
                    out.println("min DFA: \n" + detCurrent);
                    
                    out.println(current.getNumberOfStates() + " vs " + detCurrent.getNumberOfStates());
                    out.println("----------------");
                    
                    // teraz prehodime prechody na 0 a 1 - tento automat je potencialne tiez minimalny
                         
                    Automaton switchedOne = current.switchLetters();
                    boolean isNewTheSwitchedOne = minimalNFAs.tryToInsert(switchedOne);
                    if (isNewTheSwitchedOne){
                        out.println("----------------");
                        out.println(counter + "\nmin NFA: \n" + switchedOne);
                    
                        Automaton detSwitchedOne = switchedOne.minimalDFA().normalize();
                        out.println("min DFA: \n" + detSwitchedOne);
                    
                        out.println(switchedOne.getNumberOfStates() + " vs " + detSwitchedOne.getNumberOfStates());
                        out.println("----------------");
                    }
                    
                }
                counter++;
            }
        }
        
        out.println(new Integer(minimalNFAs.allMinNFAs.size()).toString());
        System.err.printf("%d languages found%n", minimalNFAs.allMinNFAs.size());
        System.out.println(counter);
        out.close();
    }
    
}
