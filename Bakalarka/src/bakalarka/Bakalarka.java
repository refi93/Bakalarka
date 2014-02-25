/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;




/**
 *
 * @author raf
 */

public class Bakalarka {
    
    
    public static void main(String[] args) throws Exception {
        
        //Experiments.fiveStateNFAs(100000);
        /*Variables.initialize();
        Experiments.readAutomataHashes();*/
        /*
        Scanner s = new Scanner(new File(Variables.automataFile));
        HashSet<Triplet> automataHashCodes = new HashSet<>();
        
        while(s.hasNext()){
            Automaton.readAutomaton(s);
            Automaton a = Automaton.readAutomaton(s);
            if (a != null){
                if(automataHashCodes.contains(a.myHashCode())) System.out.println("FAIL");
                automataHashCodes.add(a.myHashCode());
                System.out.println(a.myHashCode());
            }
        }
        */
        //Experiments.safeWordLengthExperiment(2);
        
        if (args.length == 1){
            Experiments.generateAllNFAsOfSize(Integer.valueOf(args[0]));
        }
        else Experiments.generateAllNFAsOfSize(3);
       
    }
    
}