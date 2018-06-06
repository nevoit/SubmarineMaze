package Model.algorithms.mazeGenerators;

/**
 * Created by User on 09-Apr-17.
 */
public interface IMazeGenerator {

    /**
     *
     * @param pRow
     * @param pColumn
     * @return
     */
    Maze generate(int pRow, int pColumn);

    /**
     *
     * @param pRow
     * @param pColumn
     * @return
     */
    long measureAlgorithmTimeMillis(int pRow, int pColumn);

    long measureAlgorithmTime(int pRow, int pColumn);
}

