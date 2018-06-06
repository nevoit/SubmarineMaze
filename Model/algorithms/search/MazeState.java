package Model.algorithms.search;

import algorithms.mazeGenerators.Position;
/**
 * State for maze
 *
 * @author Nevo
 * @version 3.0
 * @since 09-Apr-17
 */
public class MazeState extends AState{
    private int data;
    private Position position;

    public MazeState(double mCost, AState mCameFrom, int data, Position position) {
        super(position.toString(), mCost, mCameFrom);
        this.data = data;
        this.position = position;
    }

    public int getData() {
        return data;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "{" +
                "Data=" + data +
                ", Position=" + position +
                '}';
    }
}
