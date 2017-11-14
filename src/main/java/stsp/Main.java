package stsp;

import data.Data;
import solver.GreedySolver;
import solver.HeuristicSolver;
import solver.RandomSolver;
import solver.Solver;
import solver.SteepestSolver;
import utils.ArgParse;

/**
 *
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ArgParse arguments = new ArgParse(args);
        Data data = Data.load(arguments.dataPath);
        Solver solver = null;

        switch (arguments.solver) {
            case "random":
                solver = new RandomSolver(data);
                break;
            case "heuristic":
                solver = new HeuristicSolver(data);
                break;
            case "greedy":
                solver = new GreedySolver(data);
                break;
            case "steepest":
                solver = new SteepestSolver(data);
                break;
            default:
                throw new Exception("Unknown solver name.");
        }

        Experiment experiment = new Experiment(solver, arguments.time,
                arguments.minIterations, arguments.maxIterations, arguments.output);
        experiment.run();
    }
}
