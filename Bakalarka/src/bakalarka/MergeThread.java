/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raf
 */
public class MergeThread extends Thread{
    MinimalAutomatonHashMap candidates1, candidates2, result;
    
    
    public static MinimalAutomatonHashMap merge(MinimalAutomatonHashMap first, MinimalAutomatonHashMap second) throws InterruptedException{
        MergeThread mt = new MergeThread(first, second);
        mt.start();
        mt.join();
        return mt.result;
    }
    
            
    public MergeThread(MinimalAutomatonHashMap candidates1, MinimalAutomatonHashMap candidates2){
        // oplati sa nam insertovat mensie do vacsieho
        if (candidates1.size() > candidates2.size()){
            this.candidates1 = candidates1;
            this.candidates2 = candidates2;
        }
        else{
            this.candidates1 = candidates2;
            this.candidates2 = candidates1;
        }
    }
    
    @Override
    public void run(){
        
        for(Automaton a : candidates2.allMinNFAs){
            try {
                candidates1.tryToInsert(a);
            } catch (Exception ex) {
                Logger.getLogger(MergeThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        result = candidates1;
        candidates2.clear();
        System.err.printf("Merged %d automata%n",candidates1.size());
    }
}
