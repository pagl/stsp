package solver;

import data.Data;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import utils.OptGenerator;
import utils.Utilities;

/**
 * This class implements a greedy local search algorithm solving the Symmetric
 * Traveling Salesman Problem.
 *
 * It starts with a random solution and performs a 2-OPT (reverse/swap)
 * operation on the array generating new solutions, until it finds the first
 * better solution to the initial (at given iteration) one. When it finds a
 * better one it starts a new iteration from this solution.
 *
 * @author Patryk Gliszczynski
 * @author Mateusz Ledzianowski
 */
public class TabuSearchSolver extends Solver {

    private final Random rand;
    private final OptGenerator<Integer> optGenerator;
    private final int[] bestSolution;
    private float bestSolutionValue;

    private final Move[] possibleMoves;
    private int possibleMovesCounter;
    private float lastWorstValue;
    private final MoveComparator moveComparator;

    private int iterationsWithoutProgress;
    private final int stopLimit;

    public TabuSearchSolver(Data data) {
        super(data);
        this.rand = new Random();
        this.optGenerator = new OptGenerator(solution);
        this.bestSolution = new int[this.data.getSize()];
        int possibleMovesSize = (int) this.optGenerator.getNeighbourSize() / 100;
        if (possibleMovesSize == 0) {
            possibleMovesSize = 1;
        }
        this.possibleMoves = new Move[possibleMovesSize];
        this.stopLimit = 10/* * this.optGenerator.getNeighbourSize()*/;
        this.moveComparator = new MoveComparator();
    }
    
    @Override
    public void initialize() {
        super.initialize();
        this.bestSolutionValue = Float.MAX_VALUE;
        this.possibleMovesCounter = 0;
        this.lastWorstValue = Float.MAX_VALUE;
        this.iterationsWithoutProgress = 0;
    }

    @Override
    protected int[] next() {
        generatePossibleMoves();
        possibleMoves[0].go(solution);
        Utilities.swap(possibleMoves, 0, --possibleMovesCounter);
        if (possibleMoves[possibleMovesCounter].getValue() < bestSolutionValue) {
            System.arraycopy(this.solution, 0, this.bestSolution, 0, this.solution.length);
            bestSolutionValue = possibleMoves[possibleMovesCounter].getValue();
            iterationsWithoutProgress = 0;
        } else {
            ++iterationsWithoutProgress;
        }
        return this.solution;
    }

    @Override
    protected boolean hasNext() {
        boolean result = this.iterationsWithoutProgress < this.stopLimit;
        if (!result) {
            System.arraycopy(this.bestSolution, 0, this.solution, 0, this.solution.length);
        }
        return result;
    }

    private void generatePossibleMoves() {
        if (possibleMovesCounter > 0) {
            updateValues();
            if (possibleMoves[0].getValue() > lastWorstValue) {
                this.nSolutions += possibleMovesCounter;
                return;
            }
        }
        Move currentMove;
        possibleMovesCounter = 0;
        for (int[] currentSolution : this.optGenerator) {
            this.nSolutions++;
            currentMove = new Move(optGenerator.getLastA(), optGenerator.getLastB());
            currentMove.setValue(data.evaluate(currentSolution));
            if (possibleMovesCounter < possibleMoves.length) {
                possibleMoves[possibleMovesCounter++] = currentMove;
                if (possibleMovesCounter == possibleMoves.length) {
                    Arrays.sort(possibleMoves, moveComparator);
                }
            } else if (currentMove.getValue()
                    < possibleMoves[possibleMovesCounter - 1].getValue()) {
                possibleMoves[possibleMovesCounter - 1] = currentMove;
                updateValues();
                lastWorstValue = possibleMoves[possibleMovesCounter - 1].getValue();
            }
        }
    }

    private void updateValues() {
        for (int i = 0; i < possibleMovesCounter; ++i) {
            possibleMoves[i].setValue(data, solution);
        }
        Arrays.sort(possibleMoves, 0, possibleMovesCounter, moveComparator);
    }
}

class Move {

    protected final int it1;
    protected final int it2;
    private float value;

    protected Move(int it1, int it2) {
        this.it1 = it1;
        this.it2 = it2;
    }

    protected void setValue(float value) {
        this.value = value;
    }

    protected void setValue(Data data, int[] solution) {
        Utilities.reverse(solution, this.it1, this.it2);
        value = data.evaluate(solution);
        Utilities.reverse(solution, this.it1, this.it2);
    }

    protected float getValue() {
        return value;
    }

    protected void go(int[] solution) {
        Utilities.reverse(solution, this.it1, this.it2);
    }
}

class MoveComparator implements Comparator<Move> {

    @Override
    public int compare(Move o1, Move o2) {
        return new Float(o1.getValue()).compareTo(o2.getValue());
    }
}
