package Model.algorithms.search;
import java.util.HashSet;
import java.util.PriorityQueue;


/**
 * Best-first search is a search algorithm which explores
 * a graph by expanding the most promising node chosen
 * according to a specified rule.
 *
 * @author Nevo
 * @version 1.0
 * @since 09-Apr-17
 */

public class BestFirstSearch extends BreadthFirstSearch{

    @Override
    protected void initializeDataStructure() {
        mOpenList =new PriorityQueue<>();
        mClosedSet = new HashSet<>();
    }

    @Override
    public String getName() {
        return "Best First Search";
    }

    /**
     *  The cost per step can be used for weighted graphs, etc.
     * on functions that find the shortest path we use weight 1,
     * Other Algorithm can override this function if necessary.
     * @param pSecond
     * @return
     */
    protected double stateCost(AState pSecond)
    {
        return pSecond.getCost();
    }
}
