package Model.algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.ArrayList;

/**
 * SearchableMaze class
 *
 * @author Nevo
 * @version 4.0
 * @since 09-Apr-17
 */

public class SearchableMaze implements ISearchable{
    private Maze maze;
    private static double mDiagonalCost=1.5;

    public SearchableMaze(Maze maze) {
        this.maze = maze;
    }

    @Override
    public AState getInitialState() {
        return new MazeState(0,null,maze.getValueAtPosition(maze.getStartPosition()),maze.getStartPosition());
    }

    @Override
    public AState getGoalState() {
        return new MazeState(0,null,maze.getValueAtPosition(maze.getGoalPosition()),maze.getGoalPosition());
    }

    private double getCost(Position pFirst, Position pSecond)
    {
        int rowFirst=pFirst.getRowIndex();
        int columnFirst=pFirst.getColumnIndex();
        int rowSecond=pSecond.getRowIndex();
        int columnSecond=pSecond.getColumnIndex();

        if(Math.abs(rowFirst-rowSecond)==1 && Math.abs(columnFirst-columnSecond)==1)
            return mDiagonalCost;
        else if(Math.abs(rowFirst-rowSecond)==1 || Math.abs(columnFirst-columnSecond)==1)
            return 1;
        else
            return Double.MAX_VALUE;
    }

    @Override
    public ArrayList<AState> getAllPossibleStates(AState pSource) {

        ArrayList<AState> successors = new ArrayList<>();
        isValidState(successors, pSource,0,-1);
        isValidState(successors, pSource,-1,0 );
        isValidState(successors, pSource,0, 1);
        isValidState(successors, pSource,1,0);

        //Add the diagonal lines
        isValidState(successors, pSource,-1,-1);
        isValidState(successors, pSource,1,1);
        isValidState(successors, pSource,1,-1);
        isValidState(successors, pSource,-1 ,1);

        return successors;
    }

    /**
     * This function checks if the neighbor is valid, if yes - add to the Array-List
     * @param pSuccessors
     * @param pSource
     * @param pAddToRow
     * @param pAddToColumn
     */
    private void isValidState(ArrayList<AState> pSuccessors, AState pSource, int pAddToRow, int pAddToColumn)
    {
        int row = ((MazeState)pSource).getPosition().getRowIndex();
        int column = ((MazeState)pSource).getPosition().getColumnIndex();

        row+=pAddToRow;
        column+=pAddToColumn;

        Position position = new Position(row,column);
        MazeState neighborState = new MazeState(getCost(((MazeState)pSource).getPosition(),position),pSource,maze.getValueAtPosition(position),position);

        //this part of code we want to prevent return from where we came from
        MazeState cameFrom = (MazeState) pSource.getCameFrom();
        boolean equalToCameFrom=false;
        if(cameFrom!=null)
            neighborState.equals(cameFrom);

        //this part of code we want to prevent invalid diagonal
        boolean validDiagonal = true;
        if(Math.abs(pAddToRow)==1 && Math.abs(pAddToColumn)==1)
            validDiagonal = validDiagonal(pSuccessors,row-pAddToRow,column-pAddToColumn,pAddToRow,pAddToColumn);

        if(neighborState.getData() == 0 && !equalToCameFrom && validDiagonal) //check if the data is not equal to zero and not equal to came from and valid diagonal
            pSuccessors.add(neighborState);
    }


    /**
     * This function check if the diagonal line is valid: if we can walk in regular way it's valid
     *
     * row--        row--       row--
     * col --                   col++
     *
     * col--        Source      col++
     *
     * row++        row++       row++
     * col --                   col++
     *
     * @param pSuccessors
     * @param pRow
     * @param pColumn
     * @param pAddToRow
     * @param pAddToColumn
     * @return true or false depends on if the diagonal is valid
     */
    private boolean validDiagonal(ArrayList<AState> pSuccessors, int pRow, int pColumn , int pAddToRow, int pAddToColumn)
    {
        if(pAddToColumn==1 && pSuccessors.contains(new MazeState(0,null,0,new Position(pRow,pColumn+1))))
            return true;
        else if(pAddToRow==1 && pSuccessors.contains(new MazeState(0,null,0,new Position(pRow+1,pColumn))))
            return true;
        else if(pAddToColumn==-1 && pSuccessors.contains(new MazeState(0,null,0,new Position(pRow,pColumn-1))))
            return true;
        else if(pAddToRow==-1 && pSuccessors.contains(new MazeState(0,null,0,new Position(pRow-1,pColumn))))
            return true;

        return false;
    }
}
