package Model.algorithms.mazeGenerators;

import algorithms.search.AState;
import algorithms.search.MazeState;
import sun.awt.Mutex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Maze class
 *
 * @author Nevo
 * @since 09-Apr-17
 * @Patch 2.1: 20/5, column problem fix
 */
public class Maze implements Serializable{
    private static Mutex m = new Mutex();
    private int [][] mMaze;
    private Position mStart;
    private Position mGoal;
    private int mRow;
    private int mColumn;
    private static int MAX_UNSIGNED_MAZE_BYTE = 254;
    private static byte END_OF_TYPE = 127;
    private int indexByteArray = 0; //A global index for moving in byte array. SHOULD WE RESET THIS AT THE END???

    /**
     * CTOR
     * @param pMaze
     * @param pStart
     * @param pGoal
     */
    public Maze(int[][] pMaze, Position pStart, Position pGoal) {
        this.mMaze = pMaze;
        this.mStart = pStart;
        this.mGoal = pGoal;
        mRow = pMaze.length;
        mColumn = pMaze[0].length;
    }

    /**
     * CTOR
     * @param pMaze
     */
    public Maze(byte[] pMaze) {
        mRow = byteArrayToInt(pMaze);
        mColumn = byteArrayToInt(pMaze);
        Position start = new Position(byteArrayToInt(pMaze),byteArrayToInt(pMaze));
        Position end = new Position(byteArrayToInt(pMaze),byteArrayToInt(pMaze));
        this.mStart = start;
        this.mGoal = end;
        this.mMaze = createMazeWithByteArray(pMaze);
    }

    /**
     * A function that decodes the data from the Byte array and returns an int array.
     * Used in the constructor that receives a bye array (above)
     * @param pMaze
     * @return
     */
    private int [][] createMazeWithByteArray(byte[] pMaze)
    {
        int [][] mazeArray = new int [mRow][mColumn];

        for (int i = 0; i < this.mRow; i++)
            for (int j = 0; j < this.mColumn; j++)
                mazeArray[i][j]=unsignedByteToInt(pMaze[indexByteArray++]);

        return mazeArray;
    }

    /**
     * Returns the value after decoding from indexByteArray to END_OF_TYPE
     * @param pMaze
     * @return
     */
    private int byteArrayToInt(byte[] pMaze)
    {
        int sum=0;
        do
        {
            sum+=unsignedByteToInt(pMaze[indexByteArray]);
            indexByteArray++;
        } while(pMaze[indexByteArray]!=END_OF_TYPE);

        indexByteArray++;
        return sum;
    }

    /**
     *
     * @param pPosition
     * @return data in position
     */
    public int getValueAtPosition(Position pPosition)
    {
        if(pPosition.getRowIndex()<this.mRow && pPosition.getColumnIndex()<this.mColumn && pPosition.getRowIndex()>=0 && pPosition.getColumnIndex()>=0)
            return this.mMaze[pPosition.getRowIndex()][pPosition.getColumnIndex()];
        else
            return -1;
    }

    /**
     * A function that prints the maze.
     * In addition, prints the start(S) and end(E) position.
     */
    public void print(){
        m.lock();
        for (int i = 0; i < this.mRow; i++){
            for (int j = 0; j < this.mColumn; j++){
                if(getStartPosition().getRowIndex()==i && getStartPosition().getColumnIndex()==j)
                    System.out.print("S ");
                else if (getGoalPosition().getRowIndex()==i && getGoalPosition().getColumnIndex()==j)
                    System.out.print("E ");
                else if(this.mMaze[i][j]==1)
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println();
        }
        m.unlock();
    }

    public Position getStartPosition() {
        return mStart;
    }

    public Position getGoalPosition() {
        return mGoal;
    }

    /**
     * Test functions
     */
    protected void print(ArrayList<AState> pPath)
    {
        for (int i = 0; i < this.mRow; i++){
            for (int j = 0; j < this.mColumn; j++){
                if(getStartPosition().getRowIndex()==i && getStartPosition().getColumnIndex()==j)
                    System.out.print("S ");
                else if (getGoalPosition().getRowIndex()==i && getGoalPosition().getColumnIndex()==j)
                    System.out.print("E ");
                else if (ArrayListContains(pPath,this.mMaze[i][j],new Position(i,j)))
                {
                    if(this.mMaze[i][j]==0)
                        System.out.print("X ");
                    else System.out.print("? ");
                }
                else if(this.mMaze[i][j]==1)
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println();
        }
    }

    private boolean ArrayListContains(ArrayList<AState> pPath, int pData, Position pPosition) {
        if(pPath==null)
            return false;

        AState checkState= new MazeState(0,null,pData,pPosition);

        for (AState state : pPath)
            if (state.equals(checkState))
                return true;

        return false;
    }

    /**
     * Row,127,Column,127,startRow,127,startCol,127,endRow,127,endCol,127,dataStartWithZero
     * @return
     */
    public byte[] toByteArray()
    {
        ArrayList<Byte> dynamicByteArray = new ArrayList<>();
        insertToByteArray(dynamicByteArray,mRow);
        insertToByteArray(dynamicByteArray,mColumn);
        insertToByteArray(dynamicByteArray,mStart.getRowIndex());
        insertToByteArray(dynamicByteArray,mStart.getColumnIndex());
        insertToByteArray(dynamicByteArray,mGoal.getRowIndex());
        insertToByteArray(dynamicByteArray,mGoal.getColumnIndex());
        insertDataToByteArray(dynamicByteArray);

        return arrayListByteToArray(dynamicByteArray);
    }

    private void print(byte [] array){
        System.out.println("----- Byte print -----");
        for (int i = 0; i <array.length ; i++) {
            System.out.print(array[i] + " ");
        }

        System.out.println("\n ----- Unsigned Byte print -----");
        for (int i = 0; i <array.length ; i++) {
            System.out.print(unsignedByteToInt(array[i]) + " ");
        }

        long bytesBefore = ((mRow*mColumn) + 6)*Integer.BYTES;
        long bytesAfter = array.length*Byte.BYTES;
        System.out.println("\n ----- Details -----");
        System.out.println("Number of byte before: " + bytesBefore);
        System.out.println("Number of byte After: " + bytesAfter);
        System.out.println("You saved: " + (100.00-(double)bytesAfter*100/bytesBefore)+" %");
    }

    private byte[] arrayListByteToArray (ArrayList<Byte> pArray)
    {
        byte [] byteArray = new byte[pArray.size()];

        for (int i = 0; i < byteArray.length ; i++) {
            byteArray[i]=pArray.get(i).byteValue();
        }

        return byteArray;
    }

    private void insertDataToByteArray(ArrayList<Byte> pArray)
    {
        for (int i = 0; i < this.mRow; i++)
            for (int j = 0; j < this.mColumn; j++)
                pArray.add(toUnsignedByte(mMaze[i][j]));
    }

    private void insertDataToByteArray(ArrayList<Byte> pArray, int pInt)
    {
        int numOfMaxValue = pInt/MAX_UNSIGNED_MAZE_BYTE; //The integer that the maximum inserted in pInt
        int remainder = pInt-numOfMaxValue*MAX_UNSIGNED_MAZE_BYTE;

        if(numOfMaxValue==0)
            pArray.add(toUnsignedByte(remainder));
        else{
            for (int i = 0; i < numOfMaxValue ; i++) {
                pArray.add(toUnsignedByte(MAX_UNSIGNED_MAZE_BYTE));
                if(i!=numOfMaxValue-1)
                    pArray.add(Byte.MIN_VALUE);
                else if(remainder>0) {
                    pArray.add(Byte.MIN_VALUE);
                    pArray.add(toUnsignedByte(remainder));
                }
            }
        }

    }

    private void insertToByteArray(ArrayList<Byte> pArray, int pInt)
    {
        int numOfMaxValue = pInt/MAX_UNSIGNED_MAZE_BYTE;
        int remainder = pInt%MAX_UNSIGNED_MAZE_BYTE;

        if(numOfMaxValue==0)
            pArray.add(toUnsignedByte(remainder));
        else {
            for (int i = 0; i < numOfMaxValue; i++)
                pArray.add(toUnsignedByte(MAX_UNSIGNED_MAZE_BYTE));
            if (remainder > 0)
                pArray.add(toUnsignedByte(remainder));
        }

        pArray.add(END_OF_TYPE);
    }

    /**
     * Unsigned value of Byte. The maximum unsigned value is 254
     * 255 - value thats used for end of type
     * @param pByte
     * @return
     */
    private int unsignedByteToInt(byte pByte)
    {
        return pByte+(Byte.MAX_VALUE+1);
    }

    private byte toUnsignedByte(int pInt)
    {
        return (byte) (pInt-(Byte.MAX_VALUE+1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        byte[] other = (byte[]) o;

        return Arrays.equals(toByteArray(),other);
    }

    @Override
    public int hashCode() {
        return toByteArray() != null ? Arrays.hashCode(toByteArray()) : 0;
    }

    public Maze() {
    }

    public int[][] getmMaze() {
        return mMaze;
    }

    public void setmMaze(int[][] mMaze) {
        this.mMaze = mMaze;
    }

    public Position getmStart() {
        return mStart;
    }

    public void setmStart(Position mStart) {
        this.mStart = mStart;
    }

    public Position getmGoal() {
        return mGoal;
    }

    public void setmGoal(Position mGoal) {
        this.mGoal = mGoal;
    }

    public int getmRow() {
        return mRow;
    }

    public void setmRow(int mRow) {
        this.mRow = mRow;
    }

    public int getmColumn() {
        return mColumn;
    }

    public void setmColumn(int mColumn) {
        this.mColumn = mColumn;
    }

    public static int getMaxUnsignedMazeByte() {
        return MAX_UNSIGNED_MAZE_BYTE;
    }

    public static void setMaxUnsignedMazeByte(int maxUnsignedMazeByte) {
        MAX_UNSIGNED_MAZE_BYTE = maxUnsignedMazeByte;
    }

    public static byte getEndOfType() {
        return END_OF_TYPE;
    }

    public static void setEndOfType(byte endOfType) {
        END_OF_TYPE = endOfType;
    }

    public int getIndexByteArray() {
        return indexByteArray;
    }

    public void setIndexByteArray(int indexByteArray) {
        this.indexByteArray = indexByteArray;
    }
}
