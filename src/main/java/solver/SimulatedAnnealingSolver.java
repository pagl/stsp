package solver;

import data.Data;
import java.util.Random;
import utils.Utilities;

/**
 * This class implements a Simulated Annealing algorithm for Symmetric Traveling
 * Salesman Problem.
 * 
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */
public class SimulatedAnnealingSolver extends Solver {
    
    private final Random rand;
    private final double minTemperature = 0.00001;
    private final double alpha = 0.9;
    private final int maxIterations = 100;
    private final int[] currSolution;
    private double temperature;
    private int pointA, pointB, iteration = 0;
    private float prevScore, currScore;
    
   
    public SimulatedAnnealingSolver(Data data) {
        super(data);
        this.rand = new Random();
        this.currSolution = new int[this.data.getSize()];
    }
    
    @Override
    protected void initialize() {
        super.initialize();
        this.prevScore = this.data.evaluate(this.solution);
        this.temperature = 1.0;
    }
    
    @Override
    protected int[] next() {        
        pointA = rand.nextInt(this.data.getSize());
        do {
            pointB = rand.nextInt(this.data.getSize());
        } while(pointA == pointB);
        
        System.arraycopy(this.solution, 0, this.currSolution, 0, this.solution.length);
        Utilities.reverse(this.currSolution, Math.min(pointA, pointB), Math.max(pointA, pointB));
        
        this.currScore = this.data.evaluate(this.currSolution);        
        if (this.acceptanceProbability() > rand.nextDouble()) {         
            System.arraycopy(this.currSolution, 0, this.solution, 0, this.solution.length);
            this.prevScore = this.currScore;
        }
        
        this.nSolutions++;
        if (++this.iteration == this.maxIterations) {
           this.iteration = 0;
           this.temperature *= this.alpha;
        } 
        return this.solution;
    }
    
    private double acceptanceProbability() {
        return Math.exp(-(double) (this.currScore - this.prevScore) / (double) this.temperature);
    }

    @Override
    protected boolean hasNext() {
        return this.temperature > this.minTemperature;
    }
    
}
