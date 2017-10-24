/*
 * Mateusz Ledzianowski inf117226
 * Patryk Gliszczynski inf117288
 */
package stsp;

import data.Data;
import solver.RandomSolver;
import solver.Solver;
import utils.ArgParse;


public class Main {
    
    public static void main(String[] args) throws Exception {
        ArgParse arguments = new ArgParse(args);
        Data data = Data.load(arguments.dataPath);
        Solver solver = null;

        switch(arguments.solver) {
            case "random":
                solver = new RandomSolver(data);
                break;
            default:
                throw new Exception("Unknown solver name.");
        }
        
        Experiment experiment = new Experiment(solver, arguments.time);
        experiment.run();
        
        /* Example usage of OptGenerator 
        
        int[] array = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
        OptGenerator<Integer> optGenerator = new OptGenerator(array);
        
        for (int[] cities : optGenerator) {
            String array = "";
            for (int city : cities) {
                result += String.valueOf(city) + ", ";
            }
            System.out.println(result);
        }
        */
    }
}


