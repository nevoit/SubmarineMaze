package Model.algorithms.search;

import java.util.ArrayList;

/**
 * Created by Nevo Itzhak on 10-Apr-17.
 */
public interface ISearchable {

    AState getInitialState();
    AState getGoalState();
    ArrayList<AState> getAllPossibleStates(AState state);
}
