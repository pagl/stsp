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
        long iterStartTime, currentTime, iter = 0;
        
        do {
            iterStartTime = System.currentTimeMillis();
            Solver.Result result = this.solver.resolve();
            currentTime = System.currentTimeMillis();
            
            logger.write(String.valueOf(iter++) + ",");
            logger.write(String.valueOf(result.score) + ",");
            logger.write(String.valueOf(result.iteration) + ",");
            logger.writeln(String.valueOf(currentTime - iterStartTime));         
        } while (currentTime - startTime < this.timeLimit);
        
        long totalTime = currentTime - startTime;  
        logger.writeln("OPT_DIST," + String.valueOf(this.solver.getOptimalDistance()));
        logger.writeln("TOT_TIME," + String.valueOf(totalTime));
        logger.writeln("AVG_TIME," + String.valueOf(totalTime / (double) iter));
    }
    
}
