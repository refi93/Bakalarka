/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author raf
 * tato trieda ma za ciel pamatat si vsetky dosial najdene minimalne NFA
 * a dokazeme do nej aj vlozit nejake NFA a ona rozhodne, ci uz tam neni
 */
public class MinimalAutomatonHashMap {
    HashMap<BigInteger,ArrayList<Automaton> > AutomatonClasses = new HashMap<>();
    ArrayList<Automaton> allMinNFAs;
    public int comparison_count = 0;
    int max_collisions = 0;
    
    public MinimalAutomatonHashMap(){
        this.AutomatonClasses = new HashMap<>();
        this.allMinNFAs = new ArrayList<>();
    }
    
    
    /* overi, ci sa tu nenachadza uz nejaky iny ekvivalentny automat */
    public boolean containsEquivalent(Automaton a) throws Exception{
        boolean ret = false;
        BigInteger hash = a.myHashCode();
        if (AutomatonClasses.get(hash) != null) {
            for (Automaton previous : AutomatonClasses.get(hash)) {
                comparison_count++;
                if (previous.equivalent(a)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }
    
    
    /* ked sme si isty, ze dany automat je minimalny, tak ho mozme vlozit nasilu
    pomocou tejto metody
    */
    public void forceInsert(Automaton a) throws Exception{
        boolean isNew = true;
        BigInteger hash = a.myHashCode();
        if (isNew) {
            allMinNFAs.add(a);
            if (AutomatonClasses.get(hash) != null) {
                AutomatonClasses.get(hash).add(a);
            } else {
                AutomatonClasses.put(hash, new ArrayList<Automaton>());
                AutomatonClasses.get(hash).add(a);
            }
            
            /*if (AutomatonClasses.get(hash).size() > max_collisions){
                max_collisions = AutomatonClasses.get(hash).size();
                System.out.println(max_collisions);
            }*/
        }
    }
    
    
    public boolean tryToInsert(Automaton a) throws Exception{
        boolean isNew = !this.containsEquivalent(a);
        if (isNew){
            this.forceInsert(a);
        }
        return isNew;
    }
}
