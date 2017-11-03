package solver;

import data.Data;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * This class implements a simple heuristic algorithm solving the Symmetric 
 * Traveling Salesman Problem. 
 * 
 * It picks an initial node randomly, and iteratively finds the closest 
 * not visited nodes until the solution is found.
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */

public class HeuristicSolver extends Solver {
    
    private final Set<Integer> visited;
    private final Random rand;
    private final int[] solution;
    
    public HeuristicSolver(Data data) {
        super(data);
        this.visited = new HashSet<>();
        this.rand = new Random();
        this.solution = new int[this.array.length];
    }   
        
    @Override
    protected int[] next() {
        this.visited.clear();
        this.solution[0] = 1 + rand.nextInt(this.array.length);
        this.visited.add(this.solution[0]);
        
        for (int node = 1; node < this.array.length; node++) {
            this.solution[node] = this.nextBestNode(this.data.getNeighbors(this.solution[node - 1]), 
                                                    this.visited);
            this.visited.add(this.solution[node]);
        }
        return this.solution;
    }
    
    private int nextBestNode(float[] neighbors, Set<Integer> visited) {
        int minNode = -1;
        float minValue = Float.MAX_VALUE;
        for (int node = 1; node <= this.array.length; node++) {
            if (!visited.contains(node) && neighbors[node] < minValue) {
                minNode = node;
                minValue = neighbors[minNode];
            }
        }
        return minNode;
    }
}
