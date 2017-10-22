/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczy�ski inf117288
 */
package stsp;

import solver.Solver;
import utils.Logger;


public class Experiment {
    
    private final Solver solver;
    private final long time;
    private final Logger logger;
    
    public Experiment(Solver solver, long time) {
        this.solver = solver;
        this.time = time;
        this.logger = new Logger(String.valueOf(solver.getClass()));
    }
    
    public void run() {
        long startTime = System.currentTimeMillis();
        long currentTime, iter = 0;
        
        do {
            logger.write(String.valueOf(this.solver.run()));
            currentTime = System.currentTimeMillis();
            iter++;
        } while (currentTime - startTime < this.time);
        
        // Average time per iteration
        logger.write(String.valueOf((currentTime - startTime) / (double) iter));
        
        // Optimal distance
        logger.write(String.valueOf(this.solver.getOptimalDistance()));
    }
    
}
