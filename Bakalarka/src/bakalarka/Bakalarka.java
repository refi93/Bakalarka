/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.Iterator;



/**
 *
 * @author raf
 */

public class Bakalarka {
    
    
    public static void main(String[] args) throws Exception {
        //Variables.initialize();
        //System.out.println(Variables.WordToNumberMap);
        /*Iterator it = new TransitionsIterator(3);
        int counter = 0;
        while(it.hasNext()){
            it.next();
            counter++;
        }
        System.out.println(counter);*/
        if (args.length == 1){
            Experiments.GenerateAllNFAsOfSize(Integer.valueOf(args[0]));
        }
        else Experiments.GenerateAllNFAsOfSize(3);
        //System.out.println(Experiments.SafeWordLengthExperiment(2));
        //System.out.println(Variables.connectedCount);
    }
    
}