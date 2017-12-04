package solver;

import data.Data;
import utils.Utilities;

/**
 * This class implements an algorithm finding a random solution to the
 * Symmetric Traveling Salesman problem.
 * 
 * It repeatedly picks a random permutation, and returns the best found.
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public class RandomSolver extends Solver {
    
    private float currScore, bestScore;
    private final int[] bestSolution;
    
       
    public RandomSolver(Data data) {
        super(data);
        this.bestSolution = new int[this.data.getSize()];
    }
    
    @Override
    protected int[] next() {
        this.bestScore = Float.MAX_VALUE;
        
        for (int x = 0; x < 1e6; x++) {
            Utilities.shuffle(this.solution);
            
            currScore = this.data.evaluate(this.solution);
            if (currScore < this.bestScore) {
                this.bestScore = currScore;
                System.arraycopy(this.solution, 0, this.bestSolution, 0, this.solution.length);
            }    
            this.nSolutions++;
        }
        return this.bestSolution;
    }
    
    @Override
    protected boolean hasNext() {
        return false;
    }
}