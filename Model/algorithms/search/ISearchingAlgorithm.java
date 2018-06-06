package Model.algorithms.search;

/**
 * An interface of search algorithms
 * that provides a common functions.
 *
 * @author Nevo
 * @version 1.0
 * @since 09-Apr-17
 */

public interface ISearchingAlgorithm {

    /**
     * The search method
     * @param searchable
     * @return the solution - Solution
     * @see Solution
     */
    Solution solve(ISearchable searchable);

    /**
     * @return how many nodes were evaluated by the algorithm
     */
    int getNumberOfNodesEvaluated();

    /**
     * @return the name of the algorithm
     */
    String getName();
}
