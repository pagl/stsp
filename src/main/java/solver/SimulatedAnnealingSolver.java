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
    
    private final double initTempProbability = 0.95;
    private final double finalTemperature = 0.1;    
    private final int genTempIterations = 1000;
    private final double alpha = 0.9;
    private final double iterationsAlpha = 0.45;
    private final double iterationsWithoutImprovementRatio = 1.5;
    
    private final Random rand;
    private final int[] currSolution;
    private int pointA, pointB, iteration = 0;
    private float prevScore, currScore;
    
    private double temperature;
    private int maxIterations;
    private int maxIterationsWithoutImprovement;
    private int iterationsWithoutImprovement;
    
   
    public SimulatedAnnealingSolver(Data data) {
        super(data);
        this.rand = new Random();
        this.currSolution = new int[this.data.getSize()];
    }
    
    @Override
    protected void initialize() {
        super.initialize();
        this.prevScore = this.data.evaluate(this.solution);
        this.temperature = calculateInitialTemperature(initTempProbability);
        this.maxIterations = (int) (this.data.getSize() * (this.data.getSize() - 1) * iterationsAlpha);
        this.maxIterationsWithoutImprovement = (int) (this.maxIterations * this.iterationsWithoutImprovementRatio);
        this.iterationsWithoutImprovement = this.maxIterationsWithoutImprovement;
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
            if (this.currScore < this.prevScore) {
                this.iterationsWithoutImprovement = this.maxIterationsWithoutImprovement;
            }
            System.arraycopy(this.currSolution, 0, this.solution, 0, this.solution.length);
            this.prevScore = this.currScore;
        }
        
        this.iterationsWithoutImprovement -= 1;
        this.nSolutions++;
        if (++this.iteration == this.maxIterations) {
           this.iteration = 0;
           this.temperature *= this.alpha;
        } 
        return this.solution;
    }
    
    private double calculateInitialTemperature(double alpha) {
        double deltaSum = 0;
        for (int x = 0; x < genTempIterations; x++) {
            pointA = rand.nextInt(this.data.getSize());
            do {
                pointB = rand.nextInt(this.data.getSize());
            } while(pointA == pointB);

            System.arraycopy(this.solution, 0, this.currSolution, 0, this.solution.length);
            Utilities.reverse(this.currSolution, Math.min(pointA, pointB), Math.max(pointA, pointB));
            deltaSum += Math.abs(this.prevScore - this.data.evaluate(this.currSolution));  
        }
        return -(deltaSum / genTempIterations) / Math.log(alpha);
    } 
    
    private double acceptanceProbability() {
        return Math.exp(-(double) (this.currScore - this.prevScore) / (double) this.temperature);
    }

    @Override
    protected boolean hasNext() {
        return this.temperature > this.finalTemperature && this.iterationsWithoutImprovement > 0;
    }
    
}
