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
    private final long iterLimit;
    private final Logger logger;
    
    public Experiment(Solver solver, long timeLimit, long iterLimit, String logFileName) {
        this.solver = solver;
        this.timeLimit = timeLimit;
        this.iterLimit = iterLimit;
        this.logger = new Logger(logFileName);
    }
    
    public void run() {
        long startTime = System.nanoTime();
        long iterStartTime, currentTime, iter = 0;
        float bestScoreSoFar = Float.MAX_VALUE, scoreSum = 0;
        
        do {
            iterStartTime = System.nanoTime();
            Solver.Result result = this.solver.resolve();
            currentTime = System.nanoTime();
            bestScoreSoFar = Math.min(bestScoreSoFar, result.score);
            logger.write(String.valueOf(iter++) + ",");
            logger.write(String.valueOf(result.initialScore) + ",");
            logger.write(String.valueOf(result.score) + ",");
            logger.write(String.valueOf(bestScoreSoFar) + ",");
            logger.write(String.valueOf(result.iterations) + ",");
            logger.write(String.valueOf(result.steps) + ",");
            logger.writeln(String.valueOf((currentTime - iterStartTime) / 1e6));         
        } while (currentTime - startTime < this.timeLimit * 1e6 && 
                 iter < this.iterLimit);
        
        logger.writeln(String.valueOf(this.solver.getOptimalDistance()));
    }
    
}
