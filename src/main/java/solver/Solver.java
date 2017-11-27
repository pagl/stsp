package solver;

import data.Data;
import utils.Utilities;

/**
 * This is an abstract class representing a Solver instance.
 *
 * In order to implement your own algorithms extend this class and implement all
 * necessary methods.
 *
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */
public abstract class Solver {

    protected abstract int[] next();

    protected abstract boolean hasNext();

    public class Result {

        public int[] solution;
        public float initialScore;
        public float score;
        public int steps;
        public int nSolutions;

        public Object calculateSimilarity(int[] otherSolution) {
            int similarEdges = 0;
            for (int solutionIdx = 0; solutionIdx < solution.length; ++solutionIdx) {
                int otherIdx = 0;
                while (otherIdx < otherSolution.length
                        && solution[solutionIdx] != otherSolution[otherIdx]) {
                    ++otherIdx;
                }
                if (solution[(solutionIdx + 1) % solution.length] == otherSolution[(otherIdx + 1) % otherSolution.length]
                        || solution[(solutionIdx + 1) % solution.length] == otherSolution[(otherIdx + otherSolution.length - 1) % otherSolution.length]) {
                    ++similarEdges;
                }
            }
            return (float) similarEdges / solution.length;
        }
    }

    protected final Data data;
    protected final int[] solution;
    protected int nSolutions;

    public Solver(Data data) {
        this.data = data;
        this.solution = new int[this.data.getSize()];
    }

    public int[] getFullOptimalTour() {
        return this.data.getFullOptimalTour();
    }

    public float getOptimalDistance() {
        return this.data.getOptimalTourEval();
    }
    
    public void initialize() {
        for (int i = 1; i <= this.data.getSize(); i++) {
            this.solution[i - 1] = i;
        }
        Utilities.shuffle(this.solution);
    }

    public Result resolve(long stepLimit) {
        this.initialize();
        
        Result result = new Result();
        result.initialScore = this.data.evaluate(this.solution);
        result.steps = 0;
        this.nSolutions = 0;
        do {
            result.solution = this.next();
            result.steps++;
        } while (this.hasNext() && result.steps < stepLimit);
        result.score = this.data.evaluate(result.solution);
        result.nSolutions = this.nSolutions;

        return result;
    }
}
