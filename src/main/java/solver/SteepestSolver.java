package solver;

import data.Data;
import java.util.Arrays;
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
    private final int[] bestSolution, lastSolution;

    public SteepestSolver(Data data) {
        super(data);
        this.rand = new Random();
        this.optGenerator = new OptGenerator(this.solution);
        this.bestSolution = new int[this.data.getSize()];
        this.lastSolution = new int[this.data.getSize()];
    }

    @Override
    protected int[] next() {
        float currentScore;
        float bestScore = this.data.evaluate(this.solution);
        System.arraycopy(this.solution, 0, this.bestSolution, 0, this.bestSolution.length);
        System.arraycopy(this.solution, 0, this.lastSolution, 0, this.solution.length);

        for (int[] currentSolution : this.optGenerator) {
            this.steps++;
            currentScore = this.data.evaluate(currentSolution);
            if (currentScore < bestScore) {
                bestScore = currentScore;
                System.arraycopy(currentSolution, 0, this.bestSolution, 0, currentSolution.length);
            }
        }
        System.arraycopy(this.bestSolution, 0, this.solution, 0, this.bestSolution.length);
        return this.solution;
    }
    
    @Override
    protected boolean hasNext() {
        return !Arrays.equals(this.lastSolution, this.solution);
    }
}


