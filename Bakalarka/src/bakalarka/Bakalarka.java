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
            a.addState(i);
        }
        a.setInitialState(0);
        a.addFinalState(1);
        a.addTransition(0, 1, '0');
        a.addTransition(0, 0, '1');
        a.doTransition('1');
    }
    
}
