package Model;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.*;
import Server.*;
import Client.*;
import algorithms.search.Solution;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class responsible on the logic behind
 * Created by Nevo on 15-Jun-17.
 */
public class MyModel extends Observable implements IModel{

    private Maze mMaze;
    private Position mCurrentPosition;
    private Position mGoalPosition;
    private Solution mSolution;
    private Server mMazeGeneratingServer;
    private Server mSolveSearchProblemServer;
    private ExecutorService mThreadPoolExecutor;
    private boolean mPlayerWinTheGame;

    /**
     * CTOR
     */
    public MyModel() {
        mThreadPoolExecutor = Executors.newFixedThreadPool(ProjectProperties.getNumberOfThreads());
        mPlayerWinTheGame =false;
        raiseServers();
    }

    /**
     * This function raise the servers
     */
    private void raiseServers()
    {
        this.mMazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.mSolveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.mSolveSearchProblemServer.start();
        this.mMazeGeneratingServer.start();
    }

    /**
     * This function close the sever
     */
    private void stopServer()
    {
        mThreadPoolExecutor.shutdown();
        this.mMazeGeneratingServer.stop();
        this.mSolveSearchProblemServer.stop();
    }

    @Override
    public void generateBoard(int pRow, int pCol) {
        if(pRow<10)
            pRow = ProjectProperties.getMinRow();
        else if(pRow>100)
            pRow = 100;
        if(pCol<10)
            pCol = ProjectProperties.getMinColumn();
        else if(pCol>100)
            pCol = 100;
        createMazeWithServer(pRow,pCol);
    }

    /**
     * This function ask server for generate new maze
     * @param row
     * @param col
     */
    private void createMazeWithServer(int row, int col)
    {
        this.mThreadPoolExecutor.execute(() -> {
            try {
                try {
                    Client client = new Client(InetAddress.getLocalHost(), 5400, (inFromServer, outToServer) -> {
                        try {
                            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                            toServer.flush();
                            int[] mazeDimensions = new int[]{row, col};
                            toServer.writeObject(mazeDimensions); //send mMaze dimensions to server
                            toServer.flush();
                            byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated mMaze (compressed with MyCompressor) from server
                            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                            byte[] decompressedMaze = new byte[row*col*2 /*CHANGE SIZE ACCORDING TO YOUR MAZE SIZE*/]; //allocating byte[] for the decompressed mMaze -
                            is.read(decompressedMaze); //Fill decompressedMaze with bytes
                            this.mMaze = new Maze(decompressedMaze);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    client.communicateWithServer();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.err.println("You cant generate this mMaze!");
            }
            this.mPlayerWinTheGame =false;
            this.mSolution =null;
            this.mCurrentPosition = mMaze.getStartPosition();
            this.mGoalPosition = mMaze.getmGoal();
            notifyFromModel(1);
        });
    }

    public boolean saveBoard(String fileName){
        try{
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(fileName));
            mMaze.setmStart(this.mCurrentPosition);
            out.write(this.mMaze.toByteArray());
            out.flush();
            out.close();
        }
        catch(IOException e){
            return false;
        }
        return true;
    }

    public boolean loadBoard(String fileToLoad){
        byte[] savedMaze = new byte[0];
        try{
            InputStream in = new MyDecompressorInputStream(new FileInputStream(fileToLoad));
            savedMaze = new byte[25000];
            in.read(savedMaze);
            in.close();
            Maze loadedMaze = new Maze(savedMaze);
            this.mMaze = loadedMaze;
            Position start = new Position(mMaze.getStartPosition().getRowIndex(),mMaze.getStartPosition().getColumn());
            Position goal = new Position( mMaze.getGoalPosition().getRowIndex(),mMaze.getGoalPosition().getColumnIndex());
            this.mGoalPosition = goal;
            this.mCurrentPosition = start;
            this.mPlayerWinTheGame=false;
        }
        catch(IOException e){
            return  false;
        }

        setChanged();
        notifyObservers();
        return true;
    }

    @Override
    public void solveBoard() {
        this.mThreadPoolExecutor.execute(() -> {
            this.mMaze.setmStart(this.mCurrentPosition);
            try {
                Client client = new Client(InetAddress.getLocalHost(), 5401, (inFromServer, outToServer) -> {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        Maze maze = this.mMaze;
                        toServer.writeObject(maze); //send mMaze to server
                        toServer.flush();
                        mSolution = (Solution) fromServer.readObject(); //read generated mMaze (compressed with MyCompressor) from server

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                client.communicateWithServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            notifyFromModel(3);
        });
    }

    @Override
    public void VerifyMovement(int pNewRow, int pNewCol)
    {
        if(Math.abs(pNewCol -this.mCurrentPosition.getColumn())<=1 &&  Math.abs(pNewRow -this.mCurrentPosition.getRow())<=1)
        {
            int row = mMaze.getmRow();
            int col = mMaze.getmColumn();
            if(pNewRow <row && pNewCol <col)
                if(mMaze.getValueAtPosition(new Position(pNewRow, pNewCol))==0)
                {
                    this.mCurrentPosition =new Position(pNewRow, pNewCol);
                    notifyFromModel(2);
                    if(mCurrentPosition.getRowIndex()== mGoalPosition.getRowIndex()&& mCurrentPosition.getColumnIndex()== mGoalPosition.getColumnIndex())
                    {
                        this.mPlayerWinTheGame = true;
                        notifyFromModel();
                    }
                }
        }
    }

    @Override
    public void changePositionBy(int pRowChange, int pColumnChange)
    {
        int newRow =(mCurrentPosition.getRowIndex()+ pRowChange);
        int newCol = (mCurrentPosition.getColumnIndex()+ pColumnChange);

        VerifyMovement(newRow,newCol);
    }

    @Override
    public boolean isPlayerWinTheGame() {
        return mPlayerWinTheGame;
    }

    @Override
    public Solution getSolution() {
        return mSolution;
    }

    @Override
    public void exit() {
        stopServer();
    }

    @Override
    public int[][] getBoard() {
        return mMaze.getmMaze();
    }

    @Override
    public Position playerPosition() {
        return mCurrentPosition;
    }

    @Override
    public Position goalPosition() {
        return mGoalPosition;
    }

    private void notifyFromModel()
    {
        super.setChanged();
        super.notifyObservers();
    }

    private void notifyFromModel(Object arg)
    {
        super.setChanged();
        super.notifyObservers(arg);
    }

}
