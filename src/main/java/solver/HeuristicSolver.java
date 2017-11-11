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
    private final int[] newSolution;
    
    public HeuristicSolver(Data data) {
        super(data);
        this.visited = new HashSet<>();
        this.rand = new Random();
        this.newSolution = new int[this.solution.length];
    }   
        
    @Override
    protected int[] next() {
        this.visited.clear();
        this.newSolution[0] = 1 + rand.nextInt(this.solution.length);
        this.visited.add(this.newSolution[0]);
        
        for (int node = 1; node < this.solution.length; node++) {
            this.steps++;
            this.newSolution[node] = this.nextBestNode(this.data.getNeighbors(this.newSolution[node - 1]), 
                                                       this.visited);
            this.visited.add(this.newSolution[node]);
        }
        return this.newSolution;
    }
    
    @Override
    protected boolean hasNext() {
        return false;
    }
    
    private int nextBestNode(float[] neighbors, Set<Integer> visited) {
        int minNode = -1;
        float minValue = Float.MAX_VALUE;
        for (int node = 1; node <= this.solution.length; node++) {
            if (!visited.contains(node) && neighbors[node] < minValue) {
                minNode = node;
                minValue = neighbors[minNode];
            }
        }
        return minNode;
    }
}
