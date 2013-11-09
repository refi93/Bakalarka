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
        a.determinize();
    }
    
}
