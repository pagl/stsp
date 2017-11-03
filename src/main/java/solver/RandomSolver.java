package solver;

import data.Data;
import utils.Utilities;
import java.util.Random;

/**
 * This class implements an algorithm finding a random solution to the
 * Symmetric Traveling Salesman problem.
 * 
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
        Utilities.shuffle(this.array);
        return this.array;
    }
}