package solver;

import data.Data;
import utils.Utilities;

/**
 * This is an abstract class representing a Solver instance.
 * 
 * In order to implement your own algorithms extend this class and implement
 * all necessary methods.
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
        public int iterations;
        public int steps;
    }
    
    protected final Data data;
    protected final int[] solution;
    protected int steps;
    
    
    public Solver(Data data) {
        this.data = data;
        this.solution = new int[this.data.getSize()];
    }
    
    public float getOptimalDistance() {
        return this.data.getOptimalTourEval();
    }

    public Result resolve() { 
        for (int i = 1; i <= this.data.getSize(); i++) {
            this.solution[i - 1] = i;
        }
        Utilities.shuffle(this.solution);
        
        Result result = new Result();
        result.initialScore = this.data.evaluate(this.solution);
        result.iterations = 0;
        this.steps = 0;
        do {
            result.solution = this.next();
            result.iterations++;
        } while (this.hasNext());
        result.score = this.data.evaluate(result.solution);
        result.steps = this.steps;
        
        return result;
    }
}
