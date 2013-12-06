/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author raf
 */
public class Bakalarka {

    
    public static void main(String[] args) throws Exception {
        
        FastPrint out = new FastPrint();

        ArrayList<Automaton> allMinNFA = new ArrayList<>();
        HashMap<Integer,ArrayList<Automaton> > AutomatonClasses = new HashMap<>();
        
        long counter = 0;
        long start = System.nanoTime();
        for(int i = 1;i <= 3;i++){
            AutomatonIterator it = new AutomatonIterator(i);
            while(it.hasNext()){
                if (counter % 100000 == 0){
                    System.err.printf("%d, time: %d s%n", counter, (System.nanoTime() - start) / 1000000000);
                }
                Automaton current = it.next();
                boolean isNew = true;
                int hash = current.hashCode();
                if(AutomatonClasses.get(hash) != null){
                    for(Automaton previous : AutomatonClasses.get(hash)){
                        if(previous.equivalent(current)){
                            isNew = false;
                            break;
                        }
                    }
                }
                if (isNew){
                    out.println("----------------");
                    allMinNFA.add(current);
                    if (AutomatonClasses.get(hash) != null){
                        AutomatonClasses.get(hash).add(current);
                    }
                    else{
                        AutomatonClasses.put(hash, new ArrayList<Automaton>());
                        AutomatonClasses.get(hash).add(current);
                    }
                    out.println(counter + "\nmin NFA: \n" + current);
                    
                    Automaton detCurrent = current.minimalDFA().normalize();
                    out.println("min DFA: \n" + detCurrent);
                    
                    out.println(current.getNumberOfStates() + " vs " + detCurrent.getNumberOfStates());
                    out.println("----------------");
                }
                counter++;
            }
        }
        System.out.println(allMinNFA.size());
        out.close();
    }
    
}
