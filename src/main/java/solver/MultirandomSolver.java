package solver;

import data.Data;
import utils.Utilities;

/**
 * This class implements an algorithm finding a random solution to the Symmetric
 * Traveling Salesman problem.
 *
 * It picks multiple random permutations of nodes.
 *
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */
public class MultirandomSolver extends RandomSolver {

    private final int[] lastSolution;

    public MultirandomSolver(Data data) {
        super(data);
        this.lastSolution = new int[this.data.getSize()];
    }

    @Override
    protected int[] next() {
        float initialScore = this.data.evaluate(this.solution);
        System.arraycopy(this.solution, 0, this.lastSolution, 0, this.solution.length);
        Utilities.shuffle(this.lastSolution);
        this.nSolutions++;
        if (this.data.evaluate(this.lastSolution) < initialScore) {
            System.arraycopy(this.lastSolution, 0, this.solution, 0, this.solution.length);
        }

        return this.solution;
    }

    @Override
    protected boolean hasNext() {
        return true;
    }

    @Override
    public Result resolve(long stepLimit) {
        if (stepLimit == Long.MAX_VALUE) {
            return super.resolve(50000);
        }
        return super.resolve(stepLimit);
    }
}
