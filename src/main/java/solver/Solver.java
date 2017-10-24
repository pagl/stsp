package solver;

import data.Data;

/**
 * This is an abstract class representing a Solver instance.
 * In order to implement your own algorithms extend this class and implement
 * all necessary methods.
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public abstract class Solver {

    protected abstract int[] next();
    
    protected final Data data;
    protected final int[] array;
    
    public Solver(Data data) {
        this.data = data;
        this.array = new int[this.data.getSize()];
        
        // Sets a base solution
        for (int i = 1; i <= this.data.getSize(); i++) {
            this.array[i - 1] = i;
        }
    }
    
    public float getOptimalDistance() {
        return this.data.getOptimalTourEval();
    }

    public float run() {
        return this.data.evaluate(this.next());
    }
}
