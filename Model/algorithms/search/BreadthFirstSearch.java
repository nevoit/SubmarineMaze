package Model.algorithms.search;
import java.util.*;

/**
 * Breadth-first search (BFS) is an algorithm for traversing
 * or searching tree or graph data structures.
 * It starts at the tree root and explores the neighbor nodes first,
 * before moving to the next level neighbors.
 *
 * @author Nevo
 * @version 3.0
 * @since 09-Apr-17
 */
public class BreadthFirstSearch extends ACommonSearcher {

    protected Queue<AState> mOpenList; //A priotiy queue of stated to be evaluated

    public BreadthFirstSearch() {
        mOpenList =new LinkedList<>();
        mClosedSet = new HashSet<>();
    }

    @Override
    public Solution solve(ISearchable searchable) {
        initializeDataStructure();
        mEvaluateNodes=0;
        /*** Create the start state and set the cost to 0 ***/
        AState startState = searchable.getInitialState();
        if(startState==null) return null;

        pushOpenList(startState);

        while(mOpenList.size()>0) {
            AState state = popOpenList(); //remove the best node from OPEN
            mClosedSet.add(state); //So we wont check this state again

            if(state.equals(searchable.getGoalState()))
                return this.backTrace(state);

            ArrayList<AState> successors = searchable.getAllPossibleStates(state);//array list of all the successors
            for (AState childState : successors) {
                if (childState != null) {
                    if (!mClosedSet.contains(childState) && !mOpenList.contains(childState)) {
                        updateState(childState,state.getCost() + stateCost(childState),state);
                        pushOpenList(childState);
                    }
                    else if (oldCost(childState) > state.getCost() + stateCost(childState)) {
                        changeOldCost(childState,state.getCost() + stateCost(childState), state);
                        updateState(childState,state.getCost() + stateCost(childState),state);
                    }
                }
            }
        }
        return null;
    }

    /**
     * The cost per step can be used for weighted graphs, etc.
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
        for (AState s : mClosedSet)
            if (s.equals(pState))
            {
                s.setCost(pCost);
                s.setCameFrom(pCameFrom);
            }
        for (AState s : mOpenList)
            if (s.equals(pState))
            {
                s.setCost(pCost);
                s.setCameFrom(pCameFrom);
            }
    }

    /**
     * Check the cost of the old path
     * @param pState
     * @return the old cost
     */
    protected double oldCost(AState pState)
    {
        for (AState s : mClosedSet)
            if (s.equals(pState))
                return s.getCost();


        for (AState s : mOpenList)
            if (s.equals(pState))
                return s.getCost();

        return Double.NaN;
    }

    /**
     * Remove the best state from mOpenList
     * What is the best?
     * Depends on the compare function in AState inheritors
     * @return the best AState
     * @see AState
     * @see Comparator
     */
    protected AState popOpenList(){
        return mOpenList.remove();
    }


    protected void pushOpenList(AState pState){
        mEvaluateNodes++;
        mOpenList.offer(pState);
    }


    @Override
    public String getName() {
        return "Breadth First Search";
    }

    @Override
    protected void initializeDataStructure() {
        mOpenList =new LinkedList<>();
        mClosedSet = new HashSet<>();
    }
}
