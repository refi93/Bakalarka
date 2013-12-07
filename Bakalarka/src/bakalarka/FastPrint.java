/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bakalarka;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 *
 * @author raf
 */

/* trieda starajuca sa o vypisovanie */
public class FastPrint {
    
    String processId;
    BufferedWriter out;
    
    public FastPrint() throws FileNotFoundException, IOException{
        out = new BufferedWriter(new FileWriter(Variables.outputFile), 32768);
    }
        
    void println(String s) throws IOException{
        out.write(s + "\n");
    }
    
    void close() throws IOException{
        out.close();
    }
    
}
