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
    private final long iterMin;
    private final long iterMax;
    private final long stepLimit;
    private final Logger logger;

    public Experiment(Solver solver, long timeLimit, long iterMin, long iterMax, long stepLimit, String logFileName) {
        this.solver = solver;
        this.timeLimit = timeLimit;
        this.iterMin = iterMin;
        this.iterMax = iterMax;
        this.stepLimit = stepLimit;
        this.logger = new Logger(logFileName);
    }

    public void run() {
        long startTime = System.nanoTime();
        long iterStartTime, currentTime, iter = 0;
        float bestScoreSoFar = Float.MAX_VALUE, scoreSum = 0;

        do {
            iterStartTime = System.nanoTime();
            Solver.Result result = this.solver.resolve(stepLimit);
            currentTime = System.nanoTime();
            bestScoreSoFar = Math.min(bestScoreSoFar, result.score);
            logger.write(String.valueOf(iter++) + ",");
            logger.write(String.valueOf(result.initialScore) + ",");
            logger.write(String.valueOf(result.score) + ",");
            logger.write(String.valueOf(result.score / this.solver.getOptimalDistance()) + ",");
            logger.write(String.valueOf(bestScoreSoFar) + ",");
            logger.write(String.valueOf(result.steps) + ",");
            logger.write(String.valueOf(result.nSolutions) + ",");
            logger.write(String.valueOf((currentTime - iterStartTime) / 1e6) + ",");
            logger.writeln(String.valueOf(result.calculateSimilarity(solver.getFullOptimalTour())));
        } while (iter < this.iterMin
                || (currentTime - startTime < this.timeLimit * 1e6
                && iter < this.iterMax));

        logger.writeln(String.valueOf(this.solver.getOptimalDistance()));
    }

}
