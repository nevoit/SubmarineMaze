package Model.algorithms.mazeGenerators;

/*****************************************************************************/
/******************** Created by Tomer on 28-Apr-17. @version 2.4 ************/
/** A recursive method to generate a maze. The maze will be surrounded by
/** a wall and dived into 2 parts with a new wall being built (either vertically
/** or horizontally). In the new wall we create an opening, and then recursively
/** do this for each new half of the maze that was created.
/** Some walls being built are problematic and are dealt with specifically.
/** At the end we create a starting position at a random wall, and create a goal
/** on a random position on the opposite wall.                               **/
/******************************************************************************/

/**
 * an enum representing the orientation of the wall being built
 */
enum Orientation{ //an enum representing the direction of the wall being built
    HORIZONTAL,
    VERTICAL
}

public class MyMazeGenerator extends AMazeGenerator {

    /**
     * The generate function being overwritten. Creates the maze.
     * @param row
     * @param col
     * @return
     */
    @Override
    public Maze generate(int row, int col) {

        if (row < 10)
            row = 10;
        if (col < 10)
            col = 10;
        setRow(row);
        setColumn(col);

        mMazeArray = new int[row][col];
        //creating a frame of 1's (walls) around the maze
        for (int k = 0; k < mMazeArray.length; k++)
            for (int l = 0; l < mMazeArray[0].length; l++)
                if (k == 0 || l == 0 || k == row - 1 || l == col - 1)
                    mMazeArray[k][l] = 1;

        recAlg(new Position(1, 1), new Position(row - 2, col - 2)); //1st call to the algorithm
        createStartAndEnd();
        setRow(row);
        setColumn(col);
        return new Maze(mMazeArray, this.getStart(), this.getEnd());
    }

    /**
     * Determines the start and end points
     */
    private void createStartAndEnd(){
        wallSide W = randomWall();
        Position start = createStartAndEnd(W);
        Position  end = createStartAndEnd(oppositeWall(W));
        setStart(start);
        mMazeArray[getStart().getRowIndex()][getStart().getColumnIndex()] = 0;
        setEnd(end);
        mMazeArray[getEnd().getRowIndex()][getEnd().getColumnIndex()] = 0;
    }

    /**
     * creates the start or end on a given wall
     * @param W
     * @return
     */
    private Position createStartAndEnd(wallSide W) {
        Position P;
        int coordinate;
        switch (W) {
            case LEFT:
                do { coordinate = rangeRandom(1, getRow() - 2);}
                while (mMazeArray[coordinate][1] != 0);
                P = new Position(coordinate, 0);
                break;
            case TOP:
                do { coordinate = rangeRandom(1, getColumn() - 2);}
                while (mMazeArray[1][coordinate] != 0);
                P = new Position(0, coordinate);
                break;
            case RIGHT:
                do { coordinate = rangeRandom(1, getRow() - 2);}
                while (mMazeArray[coordinate][getColumn() - 2] != 0);
                P = new Position(coordinate, getColumn()-1);
                break;
            case BOTTOM:
                do {coordinate = rangeRandom(1, getColumn() - 2);}
                while (mMazeArray[getRow() - 2][coordinate] != 0);
                P = new Position(getRow() - 1, coordinate);
                break;
            default:
                P = null;
                break;
        }
        return P;
    }

    /**
     * The actual recursive algorithm. Receives most top left point and most bottom right point.
     * @param topLeft
     * @param botRight
     */
    private void recAlg(Position topLeft, Position botRight) { //direction: true - build vertical, false - build horizontal
        Position point = null; //the point in the maze where we will build a wall (vertically or horizontally)
        boolean problematicPoint = false; //the chosen point is between a wall and a hole, we must deal with it specifically
        boolean doubleHole = true; //if the chosen point is between two holes, we MUST choose a new point.
        Orientation orientation = determineOrientation(topLeft,botRight);
        boolean legal = false;
        if ((botRight.getRowIndex() - topLeft.getRowIndex() >= 1 && botRight.getColumnIndex() - topLeft.getColumnIndex() > 1) || /*the maze must be more than 2x2 */
                (botRight.getRowIndex() - topLeft.getRowIndex() > 1 && botRight.getColumnIndex() - topLeft.getColumnIndex() >= 1)) {
            while (doubleHole) { //continue searching for a good point to build a wall until it's not between 2 holes. {
                legal = true;
                point = new Position(rangeRandom(topLeft.getRowIndex() + 1, botRight.getRowIndex() - 1), rangeRandom(topLeft.getColumnIndex() + 1, botRight.getColumnIndex() - 1));
                doubleHole = checkDoubleHoles(point, orientation, topLeft, botRight); //check if its a double hole situation
                if (doubleHole && (area(topLeft, botRight) <= 6)){ //problematic area
                    doubleHole = false;
                    legal = false;
                }
                if(doubleHole && (area(topLeft,botRight) == 9))
                    orientation = flipOrientation(orientation);
                problematicPoint = checkProblematic(point, topLeft, botRight, orientation);
            }
            if (legal) {
                createWalls(point, topLeft, botRight, orientation, problematicPoint);
                if (orientation == Orientation.VERTICAL) {                      //call the algorithm recursively
                    recAlg(topLeft, new Position(botRight.getRowIndex(), point.getColumnIndex() - 1));
                    recAlg(new Position(topLeft.getRowIndex(), point.getColumnIndex() + 1), botRight);
                } else {
                    recAlg(topLeft, new Position(point.getRowIndex() - 1, botRight.getColumnIndex()));
                    recAlg(new Position(point.getRowIndex() + 1, topLeft.getColumnIndex()), botRight);
                }
            }
        }
    }

    /**determines the orientation in which to build the wall, according to the size of the maze
     *
     * @param topLeft
     * @param botRight
     * @return
     */
    private Orientation determineOrientation(Position topLeft, Position botRight){
        if (botRight.getRowIndex() - topLeft.getRowIndex() < (botRight.getColumnIndex() - topLeft.getColumnIndex())) { //more wide than long
            return Orientation.VERTICAL;
        } else
            return Orientation.HORIZONTAL;
    }

    /**Creates a wall between two points on the same axis and makes a passage in the wall.
     *
     * @param point
     * @param topLeft
     * @param botRight
     * @param orientation
     * @param problematicPoint
     */
    private void createWalls(Position point, Position topLeft, Position botRight, Orientation orientation, boolean problematicPoint) {
        createWall(point,topLeft,botRight,orientation);
        Position holeCoordinate; // holeCoordinate - the coordinate on the wall to get a hole
        if (!problematicPoint) {
            if (orientation == Orientation.VERTICAL) {
                if (rangeRandom(0, 1) == 0 && point.getRowIndex() > topLeft.getRowIndex()) //above point
                    holeCoordinate = new Position(rangeRandom(topLeft.getRowIndex(), point.getRowIndex() - 1), point.getColumnIndex());
                else if (point.getRowIndex() < botRight.getRowIndex())//below point
                    holeCoordinate = new Position(rangeRandom(point.getRowIndex() + 1, botRight.getRowIndex()), point.getColumnIndex());
                else
                    holeCoordinate = new Position(rangeRandom(topLeft.getRowIndex(), point.getRowIndex() - 1), point.getColumnIndex());
            } else {//horizontal
                if (rangeRandom(0, 1) == 0 && point.getColumnIndex() > topLeft.getColumnIndex()) //left of point
                    holeCoordinate = new Position(point.getRowIndex(), rangeRandom(topLeft.getColumnIndex(), point.getColumnIndex() - 1));
                else if (point.getColumnIndex() < botRight.getColumnIndex())//right of point
                    holeCoordinate = new Position(point.getRowIndex(), rangeRandom(point.getColumnIndex() + 1, botRight.getColumnIndex()));
                else
                    holeCoordinate = new Position(point.getRowIndex(), rangeRandom(topLeft.getColumnIndex(), point.getColumnIndex() - 1));
            }
        } else { //we have a problematic point that requires special attention (creates the hole next to the existing passage)
            if (orientation == Orientation.VERTICAL) {
                if (mMazeArray[topLeft.getRowIndex() - 1][point.getColumnIndex()] == 0)
                    holeCoordinate = new Position(topLeft.getRowIndex(), point.getColumnIndex());
                else
                    holeCoordinate = new Position(botRight.getRowIndex(), point.getColumnIndex());
            } else { //horizontal
                if (mMazeArray[point.getRowIndex()][botRight.getColumnIndex() + 1] == 0)
                    holeCoordinate = new Position(point.getRowIndex(), botRight.getColumnIndex());
                else
                    holeCoordinate = new Position(point.getRowIndex(), topLeft.getColumnIndex());
            }
        }
        mMazeArray[holeCoordinate.getRowIndex()][holeCoordinate.getColumnIndex()] = 0; //creates the passage
    }

    /**simply creates a wall around a given point
     *
     * @param point
     * @param topLeft
     * @param botRight
     * @param orientation
     */
    private void createWall(Position point, Position topLeft, Position botRight, Orientation orientation){
        for (int i = topLeft.getRowIndex(); i <= botRight.getRowIndex(); i++) { //the loop that builds the wall
            for (int j = topLeft.getColumnIndex(); j <= botRight.getColumnIndex(); j++) {
                if (orientation == Orientation.VERTICAL) { //build a vertical wall
                    if (j == point.getColumnIndex())
                        mMazeArray[i][j] = 1;
                } else {
                    if (i == point.getRowIndex())
                        mMazeArray[i][j] = 1;
                }
            }
        }
    }

    /**checks if the point chosen has a hole on one side.
     *
     * @param point
     * @param topLeft
     * @param botRight
     * @param orientation
     * @return
     */
    private boolean checkProblematic(Position point, Position topLeft, Position botRight, Orientation orientation) {
        boolean problematic = false;
        if (orientation == Orientation.VERTICAL) {
            if (mMazeArray[topLeft.getRowIndex() - 1][point.getColumnIndex()] == 0 ||
                    mMazeArray[botRight.getRowIndex() + 1][point.getColumnIndex()] == 0) {
                problematic = true;
            }
        } else { //horizontal
            if (mMazeArray[point.getRowIndex()][topLeft.getColumnIndex() - 1] == 0 ||
                    mMazeArray[point.getRowIndex()][botRight.getColumnIndex() + 1] == 0) {
                problematic = true;
            }
        }
        return problematic;
    }

    private int area(Position topLeft, Position botRight){
        return ( (botRight.getRowIndex() - topLeft.getRowIndex() +1)*
                (botRight.getColumnIndex() - topLeft.getColumnIndex() +1));
    }

    private Orientation flipOrientation(Orientation ort){
        if (ort == Orientation.HORIZONTAL)
            return Orientation.VERTICAL;
        else
            return Orientation.HORIZONTAL;
    }

    /**return true if the chosen point is between 2 holes. If it does, a new point must be chosen.
     *
     * @param P
     * @param orientation
     * @param topLeft
     * @param botRight
     * @return
     */
    private boolean checkDoubleHoles(Position P, Orientation orientation, Position topLeft, Position botRight){
        if(orientation == Orientation.VERTICAL){
            if(mMazeArray[topLeft.getRowIndex()-1][P.getColumnIndex()] == 0 &&
                    mMazeArray[botRight.getRowIndex()+1][P.getColumnIndex()] == 0)
                return true;
        }
       else{
            if(mMazeArray[P.getRowIndex()][topLeft.getColumnIndex()-1] == 0 &&
                    mMazeArray[P.getRowIndex()][botRight.getColumnIndex()+1] == 0)
                return true;
        }
        return false;
    }

    /**.
     * testing function
     */
    protected void testMazeGenerator() {
        System.out.println("Creating 15 increasingly growing mazes:");
        for (int i = 1; i <50 ; i++) {
            Maze M = generate(50,38);
            M.print();
            System.out.println("------------------------------------------------------");
        }
        System.out.println("FINISHED TESTING");
    }

    /**print function used for debugging
     *
     */
    private void debugPrint() {
        System.out.println();
        for (int i = 0; i<getRow(); i++) {
            System.out.print(i+" ");
            if(i<10)
                System.out.print(" ");
            for (int j = 0; j < mMazeArray[0].length; j++) {
                if(mMazeArray[i][j]==1)
                      System.out.print("██\t");
                else
                    System.out.print("   \t");
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------------");
    }
}
