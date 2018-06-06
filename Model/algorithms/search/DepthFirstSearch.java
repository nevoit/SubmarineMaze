package Model.algorithms.search;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Depth-first search (DFS) is an algorithm for traversing
 * or searching tree or graph data structures.
 * One starts at the root and explores as far as
 * possible along each branch before backtracking.
 *
 * @author Nevo
 * @version 3.0
 * @since 09-Apr-17
 */
public class DepthFirstSearch extends ACommonSearcher {

    protected Stack<AState> mOpenList; //A stack of stated to be evaluated

    /*** CTOR ***/
    public DepthFirstSearch() {
        mOpenList =new Stack<>();
        mClosedSet = new HashSet<>();
    }

    @Override
    public Solution solve(ISearchable searchable) {
        initializeDataStructure();
        mEvaluateNodes=0;

        AState startState = searchable.getInitialState();
        AState endState = searchable.getGoalState();

        if(startState==null || endState==null) return null;

        pushOpenList(startState);

        while(!mOpenList.isEmpty()) {
            AState state = popOpenList();
            mClosedSet.add(state); //same like visited flag

            if(state.equals(endState))
                return this.backTrace(state);

            ArrayList<AState> arrayList = searchable.getAllPossibleStates(state);
            for (AState childState : arrayList) {
                if (childState != null) {
                    if (!mClosedSet.contains(childState) && !mOpenList.contains(childState)) {
                        updateState(childState,state.getCost() + stateCost(childState),state);
                        pushOpenList(childState);
                    }
                    else if (oldCost(childState) > state.getCost() + stateCost(childState) && !mClosedSet.contains(childState)) {
                        changeOldCost(childState, state.getCost() + stateCost(childState), state);
                        updateState(childState, state.getCost() + stateCost(childState), state);
                    }
                }
            }
        }
        return this.backTrace(endState);
    }

    /**
     *      * The cost per step can be used for weighted graphs, etc.
     * on functions that find the shortest path we use weight 1,
     * Other Algorithm can override this function if necessary.
     * @param pSecond
     * @return
     */
    protected double stateCost(AState pSecond)
    {
        return 1;
    }

    /**
     * A function that changes the old cost to the cost of the new path
     * @param pState
     * @param pCost
     * @param pCameFrom
     */
    protected void changeOldCost(AState pState, double pCost, AState pCameFrom)
    {
        if(mClosedSet.contains(pState)) {
            for (AState s : mClosedSet)
                if (s.equals(pState)) {
                    s.setCost(pCost);
                    s.setCameFrom(pCameFrom);
                }
        }
        else if(mOpenList.contains(pState)) {
            for (AState s : mOpenList)
                if (s.equals(pState)) {
                    s.setCost(pCost);
                    s.setCameFrom(pCameFrom);
                }
        }
    }

    /**
     * Check the cost of the old path
     * @param pState
     * @return the old cost
     */
    protected double oldCost(AState pState)
    {
        if(mClosedSet.contains(pState)) {
            for (AState s : mClosedSet)
                if (s.equals(pState))
                    return s.getCost();
        }
        else if(mOpenList.contains(pState)) {
                    for (AState s : mOpenList)
                        if (s.equals(pState))
                            return s.getCost();
                }

        return Double.MAX_VALUE;
    }

    /**
     * Remove the best state from mOpenList
     * What is the best?
     * Depends on the compareTo function in AState inheritors
     * @return the best AState
     * @see AState
     * @see Comparable
     */
    protected AState popOpenList(){ //
        return mOpenList.pop();
    }

    protected void pushOpenList(AState pState){
        mEvaluateNodes++;
        mOpenList.push(pState);
    }

    @Override
    public String getName() {
        return "Depth First Search";
    }

    @Override
    protected void initializeDataStructure() {
        mOpenList =new Stack<>();
        mClosedSet = new HashSet<>();
    }
}
