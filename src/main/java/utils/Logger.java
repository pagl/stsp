/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Logger {
    
    private FileWriter fileWriter;
    private final BufferedWriter bufferedWriter;
    private final PrintWriter printWriter;
    
    public Logger(String name) {
        try {
            this.fileWriter = new FileWriter(name, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bufferedWriter = new BufferedWriter(this.fileWriter);
        this.printWriter = new PrintWriter(this.bufferedWriter);
    }
    
    public void write(String message) {
       // this.printWriter.println(message);
       System.out.println(message);  // For easier debugging
    }
}

