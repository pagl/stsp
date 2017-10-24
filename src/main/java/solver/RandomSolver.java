package solver;

import data.Data;
import utils.Utilities;
import java.util.Random;

/**
 * This class implements an algorithm finding a random solution to the
 * Symmetric Traveling Salesman problem.
 * It picks a random permutation of nodes.
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public class RandomSolver extends Solver {
    
    private final Random rand;
       
    public RandomSolver(Data data) {
        super(data);
        this.rand = new Random();
    }
    
    @Override
    protected int[] next() {
        for (int i = this.data.getSize() - 1; i > 0; --i) {
            Utilities.swap(this.array, this.rand.nextInt(i + 1), i);
        }
        return this.array;
    }
}