/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author raf
 */
public class Bakalarka {

    
    public static void main(String[] args) throws Exception {
        
        ArrayList<Automaton> allMinNFA = new ArrayList<>();
        HashMap<Integer,ArrayList<Automaton> > AutomatonClasses = new HashMap<>();
        
        int counter = 0;
        for(int i = 1;i <= 3;i++){
            AutomatonIterator it = new AutomatonIterator(i);
            while(it.hasNext()){
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
                    System.out.println("----------------");
                    allMinNFA.add(current);
                    if (AutomatonClasses.get(hash) != null){
                        AutomatonClasses.get(hash).add(current);
                    }
                    else{
                        AutomatonClasses.put(hash, new ArrayList<Automaton>());
                        AutomatonClasses.get(hash).add(current);
                    }
                    System.out.println(counter + "\nmin NFA: \n" + current);
                    
                    Automaton detCurrent = current.minimalDFA().normalize();
                    System.out.println("min DFA: \n" + detCurrent);
                    
                    System.out.println(current.getNumberOfStates() + " vs " + detCurrent.getNumberOfStates());
                    System.out.println("----------------");
                }
                counter++;
                if(counter % 10000 == 0){
                    //System.out.println("COUNTER " + counter);
                    //return;
                }
            }
        }
        System.out.println(allMinNFA.size());
    }
    
}
