package Model.algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Solution class
 */
public class Solution implements Serializable {

    private ArrayList<AState> path;

    public Solution(ArrayList<AState> path) {
        this.path=path;
    }

    public ArrayList<AState> getSolutionPath()
    {
        return this.path;
    }

    public Solution() {
    }

    public ArrayList<AState> getPath() {
        return path;
    }

    public void setPath(ArrayList<AState> path) {
        this.path = path;
    }
}
