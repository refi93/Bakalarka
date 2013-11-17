/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author raf
 */
public class Bakalarka {

    
    public static void main(String[] args) throws Exception {
        
        // TODO code application logic here
        Automaton a = new Automaton();
        int n = 5;
        for(int i = 0;i < n;i++){
            a.addState(new IntegerIdentificator(i));
        }
        
        a.setInitialStateId(new IntegerIdentificator(0));
        a.addFinalState(new IntegerIdentificator(1));
        a.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(1), '0');
        a.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(0), '1');
        a.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(1), '1');
        a.addTransition(new IntegerIdentificator(1), new IntegerIdentificator(2), '0');
        
        
        Automaton test = new Automaton();
        test.addState(new IntegerIdentificator(0));
        test.addState(new IntegerIdentificator(1));
        test.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(1), '0');
        test.addFinalState(new IntegerIdentificator(1));
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
    }
    
}
