package Model;

import Model.algorithms.mazeGenerators.Position;
import Model.algorithms.search.Solution;

/**
 * Interface for the model inside the MVVM architecture
 * Created by Nevo on 15-Jun-18.
 */
public interface IModel {
    /**
     * if(mMazeDisplayer.getmMaze() != null){ //Added since resizing before generate threw an exception
     * @param pRow
     * @param pCol
     */
    void generateBoard(int pRow, int pCol);

    /**
     * This function return the current board
     * @return
     */
    int[][] getBoard();

    /**
     * @return Position
     * @see  Position
     */
    Position playerPosition();

    /**
     * @return Position
     * @see  Position
     */
    Position goalPosition();

    /**
     * This function gets row and column to increase or decrease
     * @param pRowChange
     * @param pColumnChange
     */
    void changePositionBy(int pRowChange, int pColumnChange);

    /**
     * This function verify if your movement for pNewRow and pNewCol its legal
     * @param pNewRow
     * @param pNewCol
     */
    void VerifyMovement(int pNewRow, int pNewCol);

    /**
     * This function solve the board
     */
    void solveBoard();

    /**
     * @return boolean if the player finish the game
     */
    boolean isPlayerWinTheGame();

    /**
     * This function return solution for the board
     * @return
     * @see Solution
     */
    Solution getSolution();

    /**
     * This function does an orderly exit
     */
    void exit();

    /**
     * This function save the board for reuse
     * @param fileName
     */
    boolean saveBoard(String fileName);

    /**
     * This function loads a board that was saved in the past
     * @param fileName
     */
    boolean loadBoard(String fileName);
}
