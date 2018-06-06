package Model.algorithms.search;

import java.io.Serializable;
import java.util.Comparator;

/**
 * General State
 *
 * @author Nevo
 * @version 2.0
 * @since 09-Apr-17
 */

public abstract class AState implements Comparator<AState>,Comparable<AState>,Serializable{
    private String mState; // the mState represented by string
    private double mCost; // mCost to reach this mState
    private AState mCameFrom;

    /**
     * CTOR
     * @param mState
     * @param mCost
     * @param mCameFrom
     */
    public AState(String mState, double mCost, AState mCameFrom) {
        this.mState = mState;
        this.mCost = mCost;
        this.mCameFrom = mCameFrom;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AState state = (AState) o;

        return mState != null ? mState.equals(state.mState) : state.mState == null;
    }

    @Override
    public int hashCode() {
        return mState != null ? mState.hashCode() : 0;
    }

    public void setCameFrom(AState state)
    {
        this.mCameFrom =state;
    }

    public void setCost(double cost) {
        this.mCost = cost;
    }

    public double getCost() {
        return mCost;
    }

    public AState getCameFrom() {
        return mCameFrom;
    }

    @Override
    public int compare(AState o1, AState o2) {
        if(o1.getCost()<o2.getCost())
            return -1;
        else  if(o1.getCost()>o2.getCost())
            return 1;
        return 0;
    }

    @Override
    public int compareTo(AState o) {
        return compare(this,o);
    }

}
