package Model.algorithms.search;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import java.util.ArrayList;
import java.util.HashSet;
import static algorithms.mazeGenerators.AMazeGenerator.rangeRandom;

/**
 * An abstract class of search algorithms
 * that provides a common functions.
 *
 * @author Nevo
 * @version 3.0
 * @since 09-Apr-17
 */

public abstract class ACommonSearcher implements ISearchingAlgorithm {

    protected int mEvaluateNodes; //how many nodes were evaluated by the algorithm
    /**
     * A set of all completed states -
     * instead of using colors as in the usual algorithms,
     * this mode comes to handle gray and black mode.
     */
    protected HashSet<AState> mClosedSet;

    /*** CTOR ***/
    public ACommonSearcher(){
        mEvaluateNodes = 0;
        mClosedSet = new HashSet<>();
    }

    /**
     * A function that goes from the end to the beginning
     * and return the reverse path (Start-->....--->Goal)
     * @param goal - the goal state
     * @return Solution
     * @see Solution,AState
     */
    protected Solution backTrace(AState goal)
    {
        AState parenState = goal;
        ArrayList<AState> arrayList = new ArrayList<>();
        while(parenState!=null)
        {
            arrayList.add(0,parenState);
            parenState = parenState.getCameFrom();
        }

        return new Solution(arrayList);
    }

    /**
     * Update pState: cost=pCost and came from to pCameFrom
     * @param pState
     * @param pCost
     * @param pCameFrom
     */
    protected void updateState(AState pState, double pCost, AState pCameFrom)
    {
        pState.setCost(pCost);
        pState.setCameFrom(pCameFrom);
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return mEvaluateNodes;
    }

    @Override
    public abstract Solution solve(ISearchable searchable);

    @Override
    public abstract String getName();

    /**
     * Before we solve problem we need to clean the data struct
     */
    protected abstract void initializeDataStructure();

    /**
     * Test Functions
     */
    protected void testMazeSearchAlgorithm() {
        for (int i = 0; i < 1000 ; i++) {
            testMazeSearchAlgorithm(rangeRandom(0,200),rangeRandom(0,200));
        }

    }
    private void testMazeSearchAlgorithm(int pRow, int pCol) {
        IMazeGenerator mg = new MyMazeGenerator();
        Maze maze = mg.generate(pRow, pCol);
        System.out.println("--------------------------START " + this.getName() +" --------------------------");
        maze.print();
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        testMazeSolveProblemUsingAlgorithm(searchableMaze, this, maze);
    }
    private void testMazeSolveProblemUsingAlgorithm(ISearchable pDomain, ISearchingAlgorithm pSearcher, Maze maze) {
        Solution solution = pSearcher.solve(pDomain);
        System.out.println(String.format("'%s' algorithm - nodes evaluated: %s", pSearcher.getName(), pSearcher.getNumberOfNodesEvaluated()));
        System.out.println("Solution path:");
        ArrayList<AState> solutionPath = solution.getSolutionPath();
       // for (int i = 0; i < solutionPath.size(); i++)
         //   System.out.print(String.format("%s",solutionPath.get(i)));
        System.out.println();
        //maze.print(solutionPath);
        System.out.println("--------------------------END " + this.getName() +" --------------------------");
    }

}
