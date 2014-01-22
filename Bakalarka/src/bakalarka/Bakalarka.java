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
        if (args.length == 1){
            Experiments.GenerateAllNFAsOfSize(Integer.valueOf(args[0]));
        }
        else Experiments.GenerateAllNFAsOfSize(3);
    }
    
}