package stsp;

import solver.Solver;
import utils.Logger;

/**
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public class Experiment {
    
    private final Solver solver;
    private final long timeLimit;
    private final Logger logger;
    
    public Experiment(Solver solver, long timeLimit) {
        this.solver = solver;
        this.timeLimit = timeLimit;
        this.logger = new Logger(String.valueOf(solver.getClass()));
    }
    
    public void run() {
        long startTime = System.currentTimeMillis();
        long currentTime, iter = 0;
        
        do {
            logger.write("#" + String.valueOf(iter++) + " " + String.valueOf(this.solver.resolve()));
            currentTime = System.currentTimeMillis();
        } while (currentTime - startTime < this.timeLimit);
        
        long totalTime = currentTime - startTime;
        double averageTime = totalTime / (double) iter;
        
        // Optimal distance
        logger.write("Optimal_Distance: " + String.valueOf(this.solver.getOptimalDistance()));
        
        // Log total run time
        logger.write("Total_Time: " + String.valueOf(totalTime));
        
        // Log average time per iteration
        logger.write("Average_Time: " + String.valueOf(averageTime));
    }
    
}
