package Model.algorithms.mazeGenerators;

/**
 * A simple way to create a maze
 *
 * @author Nevo
 * @version 1.0
 * @since 09-Apr-17
 */

/**
 * enum that represent the coordinate x and y.
 */
enum coordinateType {X,Y}

public class SimpleMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int pRow, int pColumn){

     //Constraint: maze's row or column will not be sized smaller than 10
     if(pRow <10)
         pRow =10;
     if(pColumn < 10)
         pColumn =10;

        mMazeArray = new int[pRow][pColumn];
        setColumn(pColumn);
        setRow(pRow);
        init(-1);

        setRandomValuesAtArray(0,1);
        createPath();

        return new Maze(mMazeArray, getStart(), getEnd());
    }

    /**
     * This function puts random values in the array. You have to give this function bottom and top range.
     * @param pBottomRange
     * @param pTopRange
     */
    private void setRandomValuesAtArray(int pBottomRange, int pTopRange)
    {
        for (int i = 0; i < getRow(); i++)
            for (int j = 0; j < getColumn(); j++)
                mMazeArray[i][j] = rangeRandom(0,1);
    }

    /**
     * This function selects two random coordinates (on opposite borders) in the maze array
     * This function calls other functions that create a path between the selected 2 coordinates
     */
    private void createPath()
    {
        wallSide ws = randomWall();
        this.setStart(randomCoordinate(ws));
        this.setEnd(randomCoordinate(oppositeWall(ws)));

        digg(new Position(getStart().getRowIndex(), getStart().getColumnIndex()),new Position(getEnd().getRowIndex(), getEnd().getColumnIndex()));
    }

    /**
     * Function that receives 2 coordinates and creates a random path between them
     * @param pStart
     * @param pEnd
     */
    private void digg(Position pStart, Position pEnd)
    {
        int startWith = rangeRandom(0,1);

        switch (startWith)
        {
            case 0:
                int newCoordinateY = digg(pStart.getColumnIndex(),pEnd.getColumnIndex(),pStart.getRowIndex(), coordinateType.Y);
                digg(pStart.getRowIndex(),pEnd.getRowIndex(),newCoordinateY, coordinateType.X);
                break;
            default:
                int newCoordinateX = digg(pStart.getRowIndex(),pEnd.getRowIndex(),pStart.getColumnIndex(), coordinateType.X);
                digg(pStart.getColumnIndex(),pEnd.getColumnIndex(),newCoordinateX, coordinateType.Y);
                break;
        }
    }

    /**
     * Given 2 coordinates source and target + the other coordinateType that stay const + the coordinateType type X or Y.
     * @param pSource
     * @param pTarget
     * @param pSecondIndex
     * @param pCoordinateType
     * @return
     */
    private int digg(int pSource, int pTarget, int pSecondIndex , coordinateType pCoordinateType)
    {
        int pointer = pSource;
        while(pointer != pTarget)
        {
            setPath(pointer,pSecondIndex, pCoordinateType);

            if(pointer > pTarget)
                pointer--;
            else
                pointer++;
        }
        setPath(pointer,pSecondIndex, pCoordinateType);

        return pointer;
    }

    /**
     * Puts zero in the coordinate depend in the coordinate type
     * @param pFirstIndex
     * @param pSecondIndex
     * @param pCoordinateType
     */
    private void setPath(int pFirstIndex, int pSecondIndex, coordinateType pCoordinateType)
    {
        if(pCoordinateType == pCoordinateType.X)
            this.mMazeArray[pFirstIndex][pSecondIndex] = 0;
        else if(pCoordinateType == pCoordinateType.Y)
            this.mMazeArray[pSecondIndex][pFirstIndex] = 0;
    }


    /**
     * Test function
     */
    protected void testSimpleMaze(){
        for (int i = 0; i < 50; i++) {
            System.out.println("-------------------------------------");
            try {
                int xCoordinate = rangeRandom(-50,100), yCoordinate=rangeRandom(-50,100);
                generate(xCoordinate, yCoordinate);
                System.out.println(String.format("Test #" + i + " - creating a random" + xCoordinate +"X" + yCoordinate + " maze:"));
                System.out.println("Start: "+ getStart()+ "End: " + getEnd());
                printMazeArray();
                System.out.println("-------------------------------------");
            }
            catch (ExceptionInInitializerError e)
            {
                System.out.println("Exception detected");
            }
        }
    }
}
