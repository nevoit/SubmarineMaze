package View;

import Model.algorithms.mazeGenerators.Position;
import Model.algorithms.search.AState;
import Model.algorithms.search.MazeState;
import Model.algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This class extend from Canvas and draw
 * @see Canvas
 * Created by Nevo on 15-Jun-17.
 */
public class MazeDisplayer extends Canvas {

    private int [][] mMaze;
    private int mCurrentPositionRow;
    private int mCurrentPositionColumn;
    private int mGoalPositionRow;
    private int mGoalPositionColumn;
    private Solution mSolution;
    private boolean mPlayerWinTheGame;
    private boolean mMuteMusic;
    private Media mBackgroundMusic;
    private MediaPlayer mBackgroundMediaPlayer;
    private Media mWinnerMedia;
    private MediaPlayer mWinnerMediaPlayer;
    private GraphicsContext mGraphicsContext;

    /**
     * CTOR
     */
    public MazeDisplayer() {
        mPlayerWinTheGame = false;
        mBackgroundMusic = new Media(new File("./src/resources/sounds/background.mp3").toURI().toString());
        mBackgroundMediaPlayer = new MediaPlayer(mBackgroundMusic);
        mWinnerMedia = new Media(new File("./src/resources/sounds/champions.mp3").toURI().toString());
        mWinnerMediaPlayer = new MediaPlayer(mWinnerMedia);
        mMuteMusic =false;
        playBackgroundSounds();
    }

    /**
     * This function make the background music to start and play in loop
     */
    private void playBackgroundSounds()
    {
        mBackgroundMediaPlayer.setAutoPlay(true);
        mBackgroundMediaPlayer.setVolume(0.5);
        mBackgroundMediaPlayer.setOnEndOfMedia(() -> mBackgroundMediaPlayer.seek(Duration.ZERO));
    }

    /**
     * This function set the solution and redraw
     * @param solution
     */
    public void setSolution(Solution solution) {
        this.mSolution = solution;
        if(solution!=null && !mPlayerWinTheGame)
            redrawSolution();
    }

    /**
     * This function mute or unMute the sounds.
     * @return
     */
    public boolean changeMute()
    {
        if(mMuteMusic)
        {
            mBackgroundMediaPlayer.setMute(false);
            mWinnerMediaPlayer.setMute(false);
            mMuteMusic = false;
        }
        else
        {
            mBackgroundMediaPlayer.setMute(true);
            mWinnerMediaPlayer.setMute(true);
            mMuteMusic = true;
        }

        return mMuteMusic;
    }

    /**
     * This function play the winner sounds
     */
    private void playWinnerSounds()
    {
        if(!mMuteMusic && mWinnerMediaPlayer.getStatus()!= MediaPlayer.Status.PLAYING) {
            mWinnerMediaPlayer.play();
        }
        mWinnerMediaPlayer.setOnEndOfMedia(() -> mWinnerMediaPlayer = new MediaPlayer(mWinnerMedia));
    }

    /**
     * This function set the maze and redraw
     * @param mMaze
     */
    public void setMaze(int[][] mMaze) {
        this.mMaze = mMaze;
        this.mPlayerWinTheGame =false;
        this.mSolution =null;
        redraw();
    }

    /**
     * This functino return fit image for the canvas width and height
     * @param cellHeight
     * @param cellWidth
     * @param stringProperty
     * @return
     * @see Image
     */
    private Image getFitIcon(double cellHeight, double cellWidth, StringProperty [] stringProperty) {
        Image image = null;
        try {
            if (cellHeight <= 16 && cellWidth <= 16) {
                image = new Image(new FileInputStream(stringProperty[0].get()));
            } else if (cellHeight <= 24 && cellWidth <= 24) {
                image = new Image(new FileInputStream(stringProperty[1].get()));
            } else if (cellHeight <= 32 && cellWidth <= 32) {
                image = new Image(new FileInputStream(stringProperty[2].get()));
            } else {
                image = new Image(new FileInputStream(stringProperty[3].get()));
            }
        }
        catch(FileNotFoundException e)
        {
            //new AlertBox("Oops","Image file not found!");
        }
        return image;
    }

    /**
     * This function draw the maze to the canvas
     */
    public void redraw() {
        if (mMaze != null) {
            double canvasHeight = super.getHeight(), canvasWidth = super.getWidth();
            double cellHeight = (canvasHeight / mMaze.length), cellWidth = (canvasWidth / mMaze[0].length);
            StringProperty [] playerImagesArray = {imageForPlayer16X16, imageForPlayer24X24, imageForPlayer32X32, imageForPlayer64X64};
            Image player=getFitIcon(cellHeight, cellWidth,playerImagesArray);
            StringProperty [] goalImagesArray = {imageForGoal16X16, imageForGoal24X24, imageForGoal32X32, imageForGoal64X64};
            Image goal=getFitIcon(cellHeight, cellWidth,goalImagesArray);
            StringProperty [] wallImagesArray = {imageForWall16X16, imageForWall24X24, imageForWall32X32, imageForWall64X64};
            Image wall=getFitIcon(cellHeight, cellWidth,wallImagesArray);

            mGraphicsContext = this.getGraphicsContext2D();
            mGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight); //Clean the canvas

            //Draw Maze
            for (int i = 0; i < mMaze.length; i++)
                for (int j = 0; j < mMaze[i].length; j++) {
                    if(i== mGoalPositionRow && j== mGoalPositionColumn)
                    {
                        if(goal!=null)
                            mGraphicsContext.drawImage(goal, j * cellWidth, i * cellHeight, cellWidth, cellHeight); //draw image for goal position
                        else
                        {
                            mGraphicsContext.setFill(Color.GREEN);
                            mGraphicsContext.fillOval(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            mGraphicsContext.setFill(Color.BLACK);
                        }
                    }
                    else if (mMaze[i][j] == 1)
                    {
                        if(wall!=null)
                            mGraphicsContext.drawImage(wall, j * cellWidth, i * cellHeight, cellWidth, cellHeight); //draw image for wall
                        else
                            mGraphicsContext.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                    }
                }

            if(player!=null)
                mGraphicsContext.drawImage(player, mCurrentPositionColumn * cellWidth, mCurrentPositionRow * cellHeight, cellWidth, cellHeight);
            else
            {
                mGraphicsContext.setFill(Color.RED);
                mGraphicsContext.fillOval(mCurrentPositionColumn * cellWidth, mCurrentPositionRow * cellHeight, cellWidth, cellHeight);
                mGraphicsContext.setFill(Color.BLACK);
            }
        }
    }

    /**
     * This function draw the welcome text to the canvas
     */
    public void redrawWelcome() {
        double canvasHeight = super.getHeight(), canvasWidth = super.getWidth();
        mGraphicsContext = this.getGraphicsContext2D();
        mGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight); //Clean the canvas
        mGraphicsContext.setFill(Color.WHITE);
    }

    /**
     * This function draw the solution to the canvas
     */
    public void redrawSolution() {

        if (mMaze != null && mSolution != null) {
            double canvasHeight = super.getHeight(), canvasWidth = super.getWidth();
            double cellHeight = (canvasHeight / mMaze.length), cellWidth = (canvasWidth / mMaze[0].length);
            ArrayList<AState> mazeSolutionSteps = mSolution.getSolutionPath();
            StringProperty [] playerImagesArray = {imageForPlayer16X16, imageForPlayer24X24, imageForPlayer32X32, imageForPlayer64X64};
            Image player=getFitIcon(cellHeight, cellWidth,playerImagesArray);
            StringProperty [] goalImagesArray = {imageForGoal16X16, imageForGoal24X24, imageForGoal32X32, imageForGoal64X64};
            Image goal=getFitIcon(cellHeight, cellWidth,goalImagesArray);

            //Draw Maze
            for (int i = 0; i < mMaze.length; i++)
                for (int j = 0; j < mMaze[i].length; j++) {
                    if (ArrayListContains(mazeSolutionSteps,this.mMaze[i][j],new Position(i,j)))
                    {
                        mGraphicsContext.setFill(new Color(0.8824, 0.949, 0.9608, 1));
                        mGraphicsContext.fillOval(j * cellWidth + cellWidth/4, i * cellHeight + cellHeight/4, cellWidth/2, cellHeight/2);
                    }
                    if(i== mGoalPositionRow && j== mGoalPositionColumn)
                    {
                        if(goal!=null)
                            mGraphicsContext.drawImage(goal, j * cellWidth, i * cellHeight, cellWidth, cellHeight); //draw image for goal position
                        else
                        {
                            mGraphicsContext.setFill(Color.GREEN);
                            mGraphicsContext.fillOval(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                            mGraphicsContext.setFill(Color.BLACK);
                        }
                    }
                }

            if(player!=null)
                mGraphicsContext.drawImage(player, mCurrentPositionColumn * cellWidth, mCurrentPositionRow * cellHeight, cellWidth, cellHeight);
            else
            {
                mGraphicsContext.setFill(Color.RED);
                mGraphicsContext.fillOval(mCurrentPositionColumn * cellWidth, mCurrentPositionRow * cellHeight, cellWidth, cellHeight);
                mGraphicsContext.setFill(Color.BLACK);
            }
        }
    }

    public void setPlayerWinTheGame(boolean mPlayerWinTheGame) {
        this.mPlayerWinTheGame = mPlayerWinTheGame;
        if(mPlayerWinTheGame)
            winTheGame();
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

    public void winTheGame()
    {
        playWinnerSounds();
        this.mSolution =null;
        double canvasHeight = this.getHeight(),canvasWidth = this.getWidth();

        Image winnerImage=null;
        try {
            winnerImage = new Image(new FileInputStream(imageForWinner.get()));
        }
        catch(FileNotFoundException e)
        {
        }

        mGraphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        mGraphicsContext.drawImage(winnerImage,0, 0, canvasHeight, canvasWidth);
    }

    public int[][] getmMaze() {
        return mMaze;
    }
    public boolean isPlayerWinTheGame() {
        return mPlayerWinTheGame;
    }
    public void setCurrentPosition(int row, int col) {
        this.mCurrentPositionRow = row;
        this.mCurrentPositionColumn = col;
        mPlayerWinTheGame =false;
        this.mSolution =null;
        if(mMaze !=null)
            redraw();
    }
    public void setGoalPosition(int row, int col) {
        this.mGoalPositionRow = row;
        this.mGoalPositionColumn = col;
        redraw();
    }

    /**
     * This part for all the StringProperty and the getters and setters
     * **/
    private StringProperty imageForWinner = new SimpleStringProperty();
    private StringProperty imageForPlayer16X16 = new SimpleStringProperty();
    private StringProperty imageForPlayer24X24 = new SimpleStringProperty();
    private StringProperty imageForPlayer32X32 = new SimpleStringProperty();
    private StringProperty imageForPlayer64X64 = new SimpleStringProperty();
    private StringProperty imageForGoal16X16 = new SimpleStringProperty();
    private StringProperty imageForGoal24X24 = new SimpleStringProperty();
    private StringProperty imageForGoal32X32 = new SimpleStringProperty();
    private StringProperty imageForGoal64X64 = new SimpleStringProperty();
    private StringProperty imageForBackground = new SimpleStringProperty();
    private StringProperty imageForWall16X16 = new SimpleStringProperty();
    private StringProperty imageForWall24X24 = new SimpleStringProperty();
    private StringProperty imageForWall32X32 = new SimpleStringProperty();
    private StringProperty imageForWall64X64 = new SimpleStringProperty();

    public String getWinnerImage() {
        return imageForWinner.get();
    }

    public String winnerImageProperty() {
        return imageForWinner.get();
    }

    public void setWinnerImage(String winnerImage) {
        this.imageForWinner.set(winnerImage);
    }

    public String getImageForBackground() {
        return imageForBackground.get();
    }

    public String imageForBackgroundProperty() {
        return imageForBackground.get();
    }

    public void setImageForBackground(String imageForBackground) {
        this.imageForBackground.set(imageForBackground);
    }

    public String getImageForPlayer16X16() {
        return imageForPlayer16X16.get();
    }

    public StringProperty imageForPlayer16X16Property() {
        return imageForPlayer16X16;
    }

    public void setImageForPlayer16X16(String imageForPlayer16X16) {
        this.imageForPlayer16X16.set(imageForPlayer16X16);
    }

    public String getImageForPlayer24X24() {
        return imageForPlayer24X24.get();
    }

    public StringProperty imageForPlayer24X24Property() {
        return imageForPlayer24X24;
    }

    public void setImageForPlayer24X24(String imageForPlayer24X24) {
        this.imageForPlayer24X24.set(imageForPlayer24X24);
    }

    public String getImageForPlayer32X32() {
        return imageForPlayer32X32.get();
    }

    public StringProperty imageForPlayer32X32Property() {
        return imageForPlayer32X32;
    }

    public void setImageForPlayer32X32(String imageForPlayer32X32) {
        this.imageForPlayer32X32.set(imageForPlayer32X32);
    }

    public String getImageForPlayer64X64() {
        return imageForPlayer64X64.get();
    }

    public StringProperty imageForPlayer64X64Property() {
        return imageForPlayer64X64;
    }

    public void setImageForPlayer64X64(String imageForPlayer64X64) {
        this.imageForPlayer64X64.set(imageForPlayer64X64);
    }

    public String getImageForWall16X16() {
        return imageForWall16X16.get();
    }

    public StringProperty imageForWall16X16Property() {
        return imageForWall16X16;
    }

    public void setImageForWall16X16(String imageForWall16X16) {
        this.imageForWall16X16.set(imageForWall16X16);
    }

    public String getImageForWall24X24() {
        return imageForWall24X24.get();
    }

    public StringProperty imageForWall24X24Property() {
        return imageForWall24X24;
    }

    public void setImageForWall24X24(String imageForWall24X24) {
        this.imageForWall24X24.set(imageForWall24X24);
    }

    public String getImageForWall32X32() {
        return imageForWall32X32.get();
    }

    public StringProperty imageForWall32X32Property() {
        return imageForWall32X32;
    }

    public void setImageForWall32X32(String imageForWall32X32) {
        this.imageForWall32X32.set(imageForWall32X32);
    }

    public String getImageForWall64X64() {
        return imageForWall64X64.get();
    }

    public StringProperty imageForWall64X64Property() {
        return imageForWall64X64;
    }

    public void setImageForWall64X64(String imageForWall64X64) {
        this.imageForWall64X64.set(imageForWall64X64);
    }

    public String getImageForGoal16X16() {
        return imageForGoal16X16.get();
    }

    public StringProperty imageForGoal16X16Property() {
        return imageForGoal16X16;
    }

    public void setImageForGoal16X16(String imageForGoal16X16) {
        this.imageForGoal16X16.set(imageForGoal16X16);
    }

    public String getImageForGoal24X24() {
        return imageForGoal24X24.get();
    }

    public StringProperty imageForGoal24X24Property() {
        return imageForGoal24X24;
    }

    public void setImageForGoal24X24(String imageForGoal24X24) {
        this.imageForGoal24X24.set(imageForGoal24X24);
    }

    public String getImageForGoal32X32() {
        return imageForGoal32X32.get();
    }

    public StringProperty imageForGoal32X32Property() {
        return imageForGoal32X32;
    }

    public void setImageForGoal32X32(String imageForGoal32X32) {
        this.imageForGoal32X32.set(imageForGoal32X32);
    }

    public String getImageForGoal64X64() {
        return imageForGoal64X64.get();
    }

    public StringProperty imageForGoal64X64Property() {
        return imageForGoal64X64;
    }

    public void setImageForGoal64X64(String imageForGoal64X64) {
        this.imageForGoal64X64.set(imageForGoal64X64);
    }
}
