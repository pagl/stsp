package solver;

import data.Data;
import java.util.Random;
import utils.OptGenerator;

/** This class implements a steepest local search algorithm solving the Symmetric
 * Traveling Salesman Problem.
 * 
 * It starts with the initial random solution, generates all local solutions
 * using 2-OPT (reverse/swap) operation, picks the best one and moves to the
 * next iteration of the algorithm using this solution.
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public class SteepestSolver extends Solver {
    
    private final Random rand;
    private final OptGenerator<Integer> optGenerator;
    private final int[] bestSolution;

    public SteepestSolver(Data data) {
        super(data);
        this.rand = new Random();
        this.optGenerator = new OptGenerator(this.array);
        this.bestSolution = new int[this.data.getSize()];
    }

    @Override
    protected int[] next() {
        float currentScore;
        float bestScore = this.data.evaluate(this.array);
        System.arraycopy(this.array, 0, this.bestSolution, 0, this.bestSolution.length);
        
        for (int[] currentSolution : this.optGenerator) {
            currentScore = this.data.evaluate(currentSolution);
            if (currentScore < bestScore) {
                bestScore = currentScore;
                System.arraycopy(currentSolution, 0, this.bestSolution, 0, currentSolution.length);
            }
        }
        System.arraycopy(this.bestSolution, 0, this.array, 0, this.bestSolution.length);
        return this.array;
    }
    
}


