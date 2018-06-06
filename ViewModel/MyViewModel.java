package ViewModel;

import Model.IModel;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Observable;
import java.util.Observer;

/**
 * Class for the view-model inside the MVVM architecture
 * Created by Nevo on 15-Jun-17.
 */

public class MyViewModel extends Observable implements Observer {

    private IModel mIModel;
    public IntegerProperty mCurrentRow, mCurrentColumn;
    public StringProperty mStatusUpdate;

    public MyViewModel(IModel iModel) {
        this.mIModel = iModel;
        mCurrentColumn = new SimpleIntegerProperty();
        mCurrentRow = new SimpleIntegerProperty();
        mStatusUpdate = new SimpleStringProperty();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o== mIModel)
        {
            int result=0;
            try {
                result = (int) arg;
            }
            catch (Exception e)
            {
            }

            switch (result)
            {
                // 1 - generate the maze
                // 2 - set player position
                // 3 - solve the maze
                case 1:
                    Platform.runLater(()-> {
                        mStatusUpdate.set("Finished with the maze creation.");
                        mCurrentRow.set(getPlayerPositionRow());
                        mCurrentColumn.set(getPlayerPositionColumn());
                    });
                    break;
                case 2:
                    Platform.runLater(()-> {
                        mCurrentRow.set(getPlayerPositionRow());
                        mCurrentColumn.set(getPlayerPositionColumn());
                        mStatusUpdate.set("Finished with the player move");
                    });
                    break;
                case 3:
                    Platform.runLater(()-> {
                        mStatusUpdate.set("Finished solve your maze");
                    });
                    break;
            }

            setChanged();
            notifyObservers();
        }
    }

    public boolean saveBoard(String fileName){
        return mIModel.saveBoard(fileName);
    }

    public boolean loadBoard(String fileName){return mIModel.loadBoard(fileName);};

    public void exit(){this.mIModel.exit();}

    public void generateBoard(int row, int col) {
        mIModel.generateBoard(row, col);
    }

    public void solveBoard() {
        mIModel.solveBoard();
    }

    public boolean isPlayerWinTheGame() {
        return mIModel.isPlayerWinTheGame();
    }

    public Solution getSolution()
    {
        return mIModel.getSolution();
    }

    public void KeyPressed(int rowMove, int colMove) {
        mIModel.changePositionBy(rowMove,colMove);
    }

    public void MouseDrag(int row, int col) {
        mIModel.VerifyMovement(row,col);
    }

    public int getPlayerPositionRow() {
        return mIModel.playerPosition().getRow();
    }

    public int getPlayerPositionColumn() {
        return mIModel.playerPosition().getColumn();
    }

    public int getGoalPositionRow() {
        return mIModel.goalPosition().getRow();
    }

    public int getGoalPositionColumn() {
        return mIModel.goalPosition().getColumn();
    }

    public int[][] getBoard() {
        return mIModel.getBoard();
    }
}
