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

        /*Automaton a = new Automaton();
        a.addState(0);
        a.addState(1);
        a.addTransition(0, 0, '0');
        a.setInitialStateId(0);
        a.addFinalState(0);
        a.addFinalState(1);
        
        System.out.println("a: " + a);
        System.out.println("detA" + a.reverse());
        System.out.println("minDetA" + a.minimalDFA().normalize());
        
        // TODO code application logic here
        Automaton a = new Automaton();
        int n = 5;
        for(int i = 0;i < n;i++){
            a.addState(new IntegerIdentificator(i));
        }
        
        a.setInitialStateId(0);
        a.addFinalState(1);
        a.addTransition(0, 1, '0');
        a.addTransition(0, 0, '1');
        a.addTransition(0, 1, '1');
        a.addTransition(1, 2, '0');
        
        Automaton b = new Automaton();
        n = 5;
        for(int i = 0;i < n;i++){
            b.addState(new IntegerIdentificator(i));
        }
        
        b.setInitialStateId(0);
        b.addFinalState(1);
        b.addTransition(0, 1, '0');
        b.addTransition(0, 0, '1');
        //b.addTransition(0, 1, '1');
        //a.addTransition(1, 2, '0');
        
        
        System.out.println(a.equivalent(b)+ "LOLO");
        
        Automaton test = new Automaton();
        test.addState(0);
        test.addState(1);
        test.addTransition(0, 1, '0');
        test.addFinalState(1);
        test.setCurrentState(new IntegerIdentificator(0));
        test.setInitialStateId(new IntegerIdentificator(0));
        
        System.out.println("a: " + a);
        System.out.println("test: " + test);
        System.out.println("test1: " + a.equivalent(test));
        System.out.println("test2: " + a.equivalent(a));
        
        System.out.println(a.minimalDFA().equivalent(a));
        
        MatrixIterator allMat = new MatrixIterator(5);
        for(int i = 0;i < 1000;i++){
            allMat.next();
        }
        
        
        SubsetIterator allSub = new SubsetIterator(5);
        for(int i = 0;i < 15;i++){
            allSub.next(); 
        }
        
        HashMap<Character,Matrix> hashM = new HashMap<>();
        hashM.put('0', allMat.next());
        hashM.put('1', allMat.next());
        
        Automaton skuskaKonstruktora = new Automaton(hashM,new IntegerIdentificator(0),allSub.next());
        
        System.out.println(hashM);
        
        System.out.println("skuska konstruktora: " + skuskaKonstruktora);
        
        
        Automaton skuskaDeter = new Automaton();
        skuskaDeter.addState(0);
        skuskaDeter.addState(1);
        skuskaDeter.addState(2);
        skuskaDeter.addState(3);
        skuskaDeter.addTransition(0, 1, '0');
        skuskaDeter.addTransition(0, 2, '0');
        skuskaDeter.addTransition(0, 2, '1');
        skuskaDeter.addTransition(1, 2, '0');
        skuskaDeter.addTransition(2, 1, '0');
        skuskaDeter.addTransition(2, 2, '0');
        skuskaDeter.addTransition(2, 3, '1');
        skuskaDeter.addTransition(2, 3, '1');
        skuskaDeter.setInitialStateId(0);
        skuskaDeter.addFinalState(3);
        
        System.out.println(skuskaDeter);
        Automaton x = skuskaDeter.determinize();
        System.out.println(x);
        System.out.println(skuskaDeter.equivalent(a));
        */
        
        ArrayList<Automaton> allMinNFA = new ArrayList<>();
        int counter = 0;
        for(int i = 1;i <= 3;i++){
            AutomatonIterator it = new AutomatonIterator(i);
            while(it.hasNext()){
                Automaton current = it.next();
                boolean isNew = true;
                for(Automaton previous : allMinNFA){
                    //System.out.println(previous);
                    if(previous.equivalent(current)){
                        isNew = false;
                        break;
                    }
                }
                if (isNew){
                    System.out.println("----------------");
                    allMinNFA.add(current);
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
