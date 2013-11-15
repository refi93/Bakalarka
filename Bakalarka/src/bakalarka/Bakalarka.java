/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

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
        
        a.setInitialState(new IntegerIdentificator(0));
        a.addFinalState(new IntegerIdentificator(1));
        a.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(1), '0');
        a.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(0), '1');
        a.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(1), '1');
        a.addTransition(new IntegerIdentificator(1), new IntegerIdentificator(2), '0');
        
        //a.replaceStateId(new IntegerIdentificator(0), new IntegerIdentificator(0));
        
        Automaton testMinimalizacie = new Automaton(a);
        testMinimalizacie = testMinimalizacie.minimalDFA();
        System.out.println("test minimalizacie: pred " + a + " po: "+  testMinimalizacie);
        
        Automaton test = new Automaton();
        test.addState(new IntegerIdentificator(0));
        test.addState(new IntegerIdentificator(1));
        test.addTransition(new IntegerIdentificator(0), new IntegerIdentificator(1), '0');
        test.addFinalState(new IntegerIdentificator(1));
        test.setCurrentState(new IntegerIdentificator(0));
        test.setInitialState(new IntegerIdentificator(0));
        
        Automaton skuska = test.determinize();
        skuska = skuska.reverse();
        skuska = skuska.determinize();
        System.out.println(skuska);
        
        Automaton tryCartesian;
        tryCartesian = a.cartesianProduct(test);
        System.out.println("tryCartesian: " + tryCartesian);
        System.out.println("tryCartesian making 1 transition: ");
        tryCartesian.doTransition('0');
        
        Automaton reversedA = a.reverse();
        reversedA.doTransition('0');
        System.out.println("dsdsad");
        
        Automaton b = new Automaton(a);
        
        //a.doTransition('0');
        System.out.println(a.equals(b)+"dfd");
        
        IntegerIdentificator x = new IntegerIdentificator(5);
        IntegerIdentificator y = new IntegerIdentificator(109);
        
        PowerSetIdentificator z = new PowerSetIdentificator();
        z.add(x);z.add(y);
        
        PowerSetIdentificator z2 = (PowerSetIdentificator)z.copy();
        System.out.println(z == z2);
        System.out.println(z.equals(z2));
        z2.add(z.copy());
        System.out.println(z2);
        Automaton detA = a.determinize();
        detA.doTransition('1');
        
        
    }
    
}
