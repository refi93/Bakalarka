/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raf
 */
public class MergeThread extends Thread{
    MinimalAutomatonHashMap candidates1, candidates2, result;
            
    // na vstupe predpokladame dve hashMapy automatov, take, ze automaty v kazdej z nich jednotlivo nie su navzajom ekvivalentne
    public MergeThread(MinimalAutomatonHashMap candidates1, MinimalAutomatonHashMap candidates2){
        
        // chceme insertovat mensiu mnozinu do vacsej
        if (candidates1.allMinNFAs.size() > candidates2.allMinNFAs.size()){
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
        // pole automatov, ktore potom vsetky naraz vlozime do druhej hashMapy - aby sme neopokavali zbytocne pracu
        ArrayList<Automaton> automatonsToInsert = new ArrayList<>();
        for(Automaton a : candidates2.allMinNFAs){
            try {
                if(!candidates1.containsEquivalent(a)){
                    automatonsToInsert.add(a);
                }
            } catch (Exception ex) {
                Logger.getLogger(MergeThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        candidates2.AutomatonClasses.clear();
        candidates2.allMinNFAs.clear();
        
        for(Automaton a : automatonsToInsert){
            try {
                candidates1.forceInsert(a);
            } catch (Exception ex) {
                Logger.getLogger(MergeThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        automatonsToInsert.clear();
        result = candidates1;
        System.err.printf("Merged %d automata%n",candidates1.allMinNFAs.size());
    }
}
