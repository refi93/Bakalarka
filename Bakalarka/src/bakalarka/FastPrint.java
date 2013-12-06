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
public class FastPrint {
    
    String processId;
    BufferedWriter out;
    
    //FileOutputStream out;
    //BufferedOutputStream bos;
    
    public FastPrint() throws FileNotFoundException, IOException{
        //processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        out = new BufferedWriter(new FileWriter("./out.txt"), 32768);
        //this.processId = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        //this.out = new FileOutputStream("/proc/" + this.processId + "/fd/1");
        //bos = new BufferedOutputStream(out);
    }
        
    void println(String s) throws IOException{
        //final byte[] str = (s + "\n").getBytes();
        //bos.write(str);
        out.write(s + "\n");
    }
    
    void close() throws IOException{
        //bos.close();
    }
}
