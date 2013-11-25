/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author raf
 */
public class Bakalarka {

    
    public static void main(String[] args) throws Exception {
        

        Matrix x = new Matrix(5,6);
        x.set(1, 3, true);
        //x.set(0, 1, true);
        //x.set(1, 2, true);
        //x.set(2, 3, true);
        //x.set(3, 3, true);
        System.out.println(x.toString() + x.getNumericRepresentation());
        
        /*ArrayList<Automaton> allMinNFA = new ArrayList<>();
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
            }
        }
        System.out.println(allMinNFA.size());*/
    }
    
}
