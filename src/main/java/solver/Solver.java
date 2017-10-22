/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package solver;

import data.Data;


public abstract class Solver {

    protected abstract int[] next();
    
    protected final Data data;
    
    public Solver(Data data) {
        this.data = data;
    }
    
    public float getOptimalDistance() {
        return this.data.getOptimalTourEval();
    }

    public float run() {
        return this.data.evaluate(this.next());
    }
}
