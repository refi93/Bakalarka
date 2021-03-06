/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author raf
 */

/* trieda starajuca sa o vypisovanie */
public class FastPrint {
    
    String processId;
    BufferedWriter out;
    
    public FastPrint() throws FileNotFoundException, IOException{
        out = new BufferedWriter(new FileWriter(Variables.outputFile,false), 32768);
    }
    
    // konstruktor dany menom suboru - tento do suboru pridava
    public FastPrint(String fileName,boolean preservePrevious) throws IOException{
        out = new BufferedWriter(new FileWriter("./" + fileName, preservePrevious), 32768);
    }
    
    public static void cleanFile(String fileName) throws IOException{
        BufferedWriter pom = new BufferedWriter(new FileWriter("./" + fileName), 32768);
        pom.close();
    }
        
    void println(String s) throws IOException{
        out.write(s + "\n");
    }
    
    void close() throws IOException{
        out.close();
    }
    
}
