package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public final class Logger {
    
    private final String fileName;
    private boolean debugMode;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;
    
    
    public Logger(String name) {
        this.fileName = name + ".res";
        this.initialize();
    }
    
    public Logger(String name, boolean debugMode) {
        this.fileName = name;
        this.debugMode = debugMode;
        this.initialize();
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.bufferedWriter.close();
        super.finalize();
    }
    
    protected void initialize() {
        try {
            this.fileWriter = new FileWriter(this.fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bufferedWriter = new BufferedWriter(this.fileWriter);
        this.printWriter = new PrintWriter(this.bufferedWriter, true);
    }
    
    public void write(String message) {
        this.printWriter.print(message);
        if (this.debugMode)
            System.out.print(message);
    }
    
    public void writeln(String message) {
        this.printWriter.println(message);
        if (this.debugMode)
            System.out.println(message);
    }
}

