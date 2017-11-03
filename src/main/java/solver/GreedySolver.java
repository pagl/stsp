package solver;

import data.Data;
import java.util.Random;
import utils.OptGenerator;

/** 
 * This class implements a greedy local search algorithm solving the Symmetric
 * Traveling Salesman Problem.
 * 
 * It starts with a random solution and performs a 2-OPT (reverse/swap) operation 
 * on the array generating new solutions, until it finds the first better 
 * solution to the initial (at given iteration) one. When it finds a better one
 * it starts a new iteration from this solution.
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public class GreedySolver extends Solver {
    
    private final Random rand;
    private final OptGenerator<Integer> optGenerator;

    public GreedySolver(Data data) {
        super(data);
        this.rand = new Random();
        this.optGenerator = new OptGenerator(array);
    }

    @Override
    protected int[] next() {
        float initialScore = this.data.evaluate(this.array);

        for (int[] currentSolution : this.optGenerator) {
            if (this.data.evaluate(currentSolution) < initialScore) {
                break;
            }
        }
        return this.array;
    }
    
}

