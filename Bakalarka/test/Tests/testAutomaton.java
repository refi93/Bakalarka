/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tests;

import bakalarka.Automaton;
import bakalarka.Identificator;
import bakalarka.IntegerIdentificator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author raf
 */
public class testAutomaton {
    
    public testAutomaton() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void testAddState() throws Exception {

        
        // test adding a single state
        Automaton test = new Automaton();
        test.addState(0);
        Identificator id = test.getState(new IntegerIdentificator(0)).getId();

        assertEquals("id of state must be 0", new IntegerIdentificator(0), id);
        System.out.println("1: add state test OK");
    }
    
    @Test
    public void testEquivalence() throws Exception {

        
        // NFA for words beginning and ending with '0'
        Automaton test = new Automaton();
        for(int i = 0;i < 8;i++){
            test.addState(i);
        }
        test.addFinalState(5); test.addFinalState(6);
        test.setInitialStateId(0);
        test.addTransition(0, 1, '1'); 
        test.addTransition(0, 7, '0');
        test.addTransition(1, 0, '1');
        test.addTransition(1, 7, '0');
        test.addTransition(2, 4, '0');
        test.addTransition(2, 5, '1');
        test.addTransition(3, 4, '0');
        test.addTransition(3, 5, '1');
        test.addTransition(4, 5, '0');
        test.addTransition(4, 6, '1');
        test.addTransition(5, 5, '0');
        test.addTransition(5, 5, '1');
        test.addTransition(6, 6, '0');
        test.addTransition(6, 5, '1');
        test.addTransition(7, 2, '0');
        test.addTransition(7, 2, '1');
        
        Automaton result = new Automaton();
        for(int i = 0;i < 5;i++){
            result.addState(i);
        }
        
        result.setInitialStateId(0);
        result.addFinalState(4);
        result.addTransition(0, 0, '1');
        result.addTransition(0, 1, '0');
        result.addTransition(1, 2, '0');
        result.addTransition(1, 2, '1');
        result.addTransition(2, 3, '0');
        result.addTransition(2, 4, '1');
        result.addTransition(3, 4, '0');
        result.addTransition(3, 4, '1');
        result.addTransition(4, 4, '0');
        result.addTransition(4, 4, '1');

        // check if multiply(10,5) returns 50
        assertTrue("test must be equivalent to result", result.equivalent(test));
        System.out.println("2: automaton equivalence test OK");
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
