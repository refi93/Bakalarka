/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;


/**
 *
 * @author raf
 */
public class Bakalarka {

    
    public static void main(String[] args) throws Exception {
        Variables.initializeWordToNumberMap(); // nainizializovanie mapy, kde si k slovu pamatame cislo - aby sme vedeli efektivne hashovat
        
        
        FastPrint out = new FastPrint();

        
        MinimalAutomatonHashMap minimalNFAs = new MinimalAutomatonHashMap();
        
        long counter = 0;
        long start = System.nanoTime();
        
        for (int i = 1; i <= 3; i++) {
            // najprv rozdelime pracu slaveom
            //AutomatonIterator it = new AutomatonIterator(i);
            ArrayList<AutomatonAnalyzer> slaves = new ArrayList<>();
            for(int j = 0;j < Variables.numberOfCores;j++){
                slaves.add(new AutomatonAnalyzer(i,Variables.numberOfCores,j));
            }
            for(AutomatonAnalyzer slave : slaves){
                slave.start();
            }
            
            for(AutomatonAnalyzer slave : slaves){
                slave.join();
            }
            /*while (it.hasNext()) {
                if (counter % 100000 == 0) {
                    System.err.printf("%d, time: %d s%n", counter, (System.nanoTime() - start) / 1000000000);
                }
                Automaton current = it.next();

                // pokusime sa vlozit automat, ak je novy, tak ho vypiseme
                //boolean isNew = minimalNFAs.tryToInsert(current);
                slaves.get(i % slaves.size()).receiveAutomaton(current);
                counter++;
            }*/

            // pozbierame pracu od slaveov a pospajame dokopy
            for (AutomatonAnalyzer slave : slaves) {
                for (Automaton current : slave.minimalNFAs.allMinNFAs) {
                    boolean isNew = Variables.allMinimalNFAs.tryToInsert(current);
                    if (isNew) {
                        out.println("----------------");
                        out.println(counter + "\nmin NFA: \n" + current);

                        Automaton detCurrent = current.minimalDFA().normalize();
                        out.println("min DFA: \n" + detCurrent);

                        out.println(current.getNumberOfStates() + " vs " + detCurrent.getNumberOfStates());
                        out.println("----------------");

                        // teraz prehodime prechody na 0 a 1 - tento automat je potencialne tiez minimalny
                        Automaton switchedOne = current.switchLetters();
                        boolean isNewTheSwitchedOne = Variables.allMinimalNFAs.tryToInsert(switchedOne);
                        if (isNewTheSwitchedOne) {
                            out.println("----------------");
                            out.println(counter + "\nmin NFA: \n" + switchedOne);

                            Automaton detSwitchedOne = switchedOne.minimalDFA().normalize();
                            out.println("min DFA: \n" + detSwitchedOne);

                            out.println(switchedOne.getNumberOfStates() + " vs " + detSwitchedOne.getNumberOfStates());
                            out.println("----------------");
                        }

                    }
                }
            }
        }
        
        out.println(new Integer(Variables.allMinimalNFAs.allMinNFAs.size()).toString());
        System.err.printf("%d languages found%n", Variables.allMinimalNFAs.allMinNFAs.size());
        System.out.println(Variables.counter);
        //System.out.println("Safe length of words: " + minimalNFAs.max);
        out.close();
    }
    
}
