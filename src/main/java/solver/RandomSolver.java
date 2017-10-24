/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package solver;

import data.Data;
import utils.Utilities;

import java.util.Random;


public class RandomSolver extends Solver {
    
    private final Random rand;
    private final int[] array;
    
    public RandomSolver(Data data) {
        super(data);
        
        this.array = new int[this.data.getSize()];
        for (int i = 1; i <= this.data.getSize(); i++) {
            this.array[i - 1] = i;
        }
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