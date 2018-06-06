package Model.algorithms.mazeGenerators;
/**
 * An abstract class of maze generation
 * that provides a common functions.
 *
 * @author Nevo
 * @version 1.0
 * @since 09-Apr-17
 */

/**
 * enum that represent the walls direction in the maze.
 */
enum wallSide {LEFT,TOP,RIGHT,BOTTOM}

public abstract class  AMazeGenerator implements IMazeGenerator {
    protected int[][] mMazeArray;
    private int mRow;
    private int mColumn;
    private Position mStart;
    private Position mEnd;

    public abstract Maze generate(int pRow, int pColumn);

    /**
     * A function that initializes the maze to the given number.
     * @param pInitNum
     */
    protected void init(int pInitNum)
    {
        if(mMazeArray ==null)
            throw new ExceptionInInitializerError("You cant initialize the array without do new");
        else
        {
            for (int i = 0; i < this.mRow; i++)
                for (int j = 0; j < this.mColumn; j++)
                    this.mMazeArray[i][j]=pInitNum;
        }
    }

    /**
     *
     * @param pMinimum
     * @param pMaximum
     * @return int number in the range
     */
    public static int rangeRandom(int pMinimum, int pMaximum) {
        int rand = pMinimum + (int) (Math.random() * ((pMaximum-pMinimum)+1));

        return rand;
    }

    @Override
    public long measureAlgorithmTimeMillis(int pRow, int pColumn) {
        long before = System.currentTimeMillis();
        generate(pRow, pColumn);
        long after = System.currentTimeMillis();

        return (after - before);
    }

    @Override
    public long measureAlgorithmTime(int pRow, int pColumn) {
        return measureAlgorithmTimeMillis(pRow,pColumn);
    }

    /**
     *  Randomly returns wall direction
     * @return wallSide - enum
     */
    protected wallSide randomWall() {
        int rand =(int) (Math.random() * 4);

        switch (rand)
        {
            case 0:
                return wallSide.LEFT;
            case 1:
                return wallSide.TOP;
            case 2:
                return wallSide.RIGHT;
            case 3:
                return wallSide.BOTTOM;
            default: return wallSide.LEFT;
        }
    }

    /**
     * A test function that allows printing the maze
     */
    protected void printMazeArray(){
        for (int i = 0; i < this.getRow() ; i++){
            for (int j = 0; j < this.getColumn(); j++){
                if(getStart().getRowIndex()==i && getStart().getColumnIndex()==j)
                    System.out.print("S ");
                else if (getEnd().getRowIndex()==i && getEnd().getColumnIndex()==j)
                    System.out.print("E ");
                else
                    System.out.print(this.mMazeArray[i][j]+" ");
            }
            System.out.println();
        }
    }

    /**
     * A function that receives a wall and returns a point on it
     * @param pWallSide
     * @return Position - {x,y}
     * @see Position
     */
    protected Position randomCoordinate(wallSide pWallSide)
    {
        int otherCoordinate;
        if(pWallSide== pWallSide.LEFT) {
            otherCoordinate = rangeRandom(0, getRow() - 1);
            return new Position(otherCoordinate,0);
        }
        else if(pWallSide==pWallSide.TOP) {
            otherCoordinate = rangeRandom(0, getColumn() - 1);
            return new Position(0,otherCoordinate);
        }
        else if(pWallSide==pWallSide.RIGHT) {
            otherCoordinate = rangeRandom(0, getRow() - 1);
            return new Position(otherCoordinate, getColumn()-1);
        }
        else {
            otherCoordinate = rangeRandom(0, getColumn() - 1);
            return new Position(getRow()-1,otherCoordinate);
        }
    }

    /**
     * Returns the opposite wall from the wall the function receives
     * @param pWallSide
     * @return wallSide - enum
     */
    protected wallSide oppositeWall(wallSide pWallSide)
    {
        if(pWallSide==wallSide.BOTTOM)
            return wallSide.TOP;
        else if(pWallSide==wallSide.LEFT)
            return wallSide.RIGHT;
        else if(pWallSide==wallSide.RIGHT)
            return  wallSide.LEFT;
        else
            return wallSide.BOTTOM;
    }

    protected Position getStart() {
        return mStart;
    }

    protected void setStart(Position pStart) {
        this.mStart = pStart;
    }

    protected Position getEnd() {
        return mEnd;
    }

    protected void setEnd(Position pEnd) {
        this.mEnd = pEnd;
    }

    protected void setRow(int pRow) {
        this.mRow = pRow;
    }

    protected void setColumn(int pColumn) {
        this.mColumn = pColumn;
    }

    protected int getRow() {
        return mRow;
    }

    protected int getColumn() {
        return mColumn;
    }
}
