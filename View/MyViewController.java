package View;

import ViewModel.MyViewModel;
import Model.algorithms.search.Solution;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import java.util.Observable;
import java.util.Observer;

/**
 * Class for the view inside the MVVM architecture
 * Created by Nevo on 16-Jun-17.
 */
public class MyViewController implements Observer,IView {

    private final int mDefualtMazeRow = 10;
    private final int mDefualtMazeColumn = 10;
    private Scene mScene;
    private Stage mPrimaryStage;

    @FXML
    public MyViewModel mViewModel;
    public MazeDisplayer mMazeDisplayer;
    public javafx.scene.control.TextField textField_RowNumber;
    public javafx.scene.control.TextField textField_ColumnsNumber;
    public javafx.scene.control.Button btn_generateBoard;
    public javafx.scene.control.Button btn_mute;
    public javafx.scene.control.Button btn_solve;
    public javafx.scene.control.MenuItem OPTN_SAVE;
    public javafx.scene.control.Label lbl_currentRow;
    public javafx.scene.control.Label lbl_currentColumn;
    public javafx.scene.control.Label lbl_status;


    public void setViewModel(MyViewModel viewModel) {
        this.mViewModel = viewModel;
        lbl_currentRow.textProperty().bind(viewModel.mCurrentRow.asString());
        lbl_currentColumn.textProperty().bind(viewModel.mCurrentColumn.asString());
        lbl_status.textProperty().bindBidirectional(viewModel.mStatusUpdate);
        btn_solve.setDisable(true);
        OPTN_SAVE.setDisable(true);
        mMazeDisplayer.redrawWelcome();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o== mViewModel)
        {
            btn_generateBoard.setDisable(false);
            btn_solve.setDisable(false);
            displayBoard();
        }
    }

    @Override
    public void displayBoard(){
        btn_generateBoard.setDisable(true);

        Platform.runLater( () -> {
            mMazeDisplayer.setMaze(mViewModel.getBoard());
            mMazeDisplayer.setCurrentPosition(mViewModel.getPlayerPositionRow(), mViewModel.getPlayerPositionColumn());
            mMazeDisplayer.setGoalPosition(mViewModel.getGoalPositionRow(), mViewModel.getGoalPositionColumn());
            OPTN_SAVE.setDisable(false);

            if(mViewModel.isPlayerWinTheGame()) {
                btn_solve.setDisable(true);
                mMazeDisplayer.requestFocus();
                mMazeDisplayer.setPlayerWinTheGame(mViewModel.isPlayerWinTheGame());
            }
            Solution solution = mViewModel.getSolution();
            if(solution!=null){
                btn_solve.setDisable(true);
                mMazeDisplayer.requestFocus();
                mMazeDisplayer.setSolution(solution);
            }
            btn_generateBoard.setDisable(false);

        } );
    }

    public void createNewMaze() {
        int row,col;
        try{
            row = Integer.valueOf(textField_RowNumber.getText());
            col = Integer.valueOf(textField_ColumnsNumber.getText());
        }
        catch (Exception e) /**toDo: Set the default by the property file **/
        {
            row = mDefualtMazeRow;
            col = mDefualtMazeColumn;
        }
        this.mViewModel.generateBoard(row, col);
    }

    public void setResizeEvent(Scene scene)
    {
        this.mScene =scene;

            scene.widthProperty().addListener((observable, oldValue, newValue) -> {
                if(mMazeDisplayer.getmMaze() != null) { //Added since resizing before generate threw an exception
                    mMazeDisplayer.setWidth(scene.getWidth() * 0.7);
                    displayBoard();
                }
            });
            scene.heightProperty().addListener((observable, oldValue, newValue) -> {
                if(mMazeDisplayer.getmMaze() != null) { //Added since resizing before generate threw an exception
                    mMazeDisplayer.setHeight(scene.getHeight() * 0.7);
                    displayBoard();
                }
            });
    }

    public void musicMute()
    {
        boolean notMute= mMazeDisplayer.changeMute();
        if(notMute)
            btn_mute.setText("Unmute");
        else
            btn_mute.setText("Mute");
    }

    public void MouseZoom(ScrollEvent event) {
        if(event.isControlDown() && mScene !=null && !mMazeDisplayer.isPlayerWinTheGame() && mMazeDisplayer.getmMaze()!=null) {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            double newWidth = mMazeDisplayer.getWidth() * zoomFactor;
            double newHeight = mMazeDisplayer.getHeight() * zoomFactor;
            if((mScene.getWidth()*0.75)>newWidth && (mScene.getHeight()*0.95)>newHeight && (mScene.getWidth()*0.3)<newWidth && (mScene.getHeight()*0.3)<newHeight)
            {
                mMazeDisplayer.setWidth(newWidth);
                mMazeDisplayer.setHeight(newHeight);
                displayBoard();
                event.consume();
            }
        }
    }

    public void KeyPressed(KeyEvent keyEvent){
        if(!mMazeDisplayer.isPlayerWinTheGame() && mMazeDisplayer.getmMaze()!=null) {
            mViewModel.mStatusUpdate.set("You try to move your player with the keyboard. Please wait...");
            int rowMove = 0, colMove = 0;

            KeyCode keyCode = keyEvent.getCode();
            switch (keyCode) {
                case UP:
                case W:
                    rowMove--;
                    break;
                case X:
                case DOWN:
                case S:
                    rowMove++;
                    break;
                case RIGHT:
                case D:
                    colMove++;
                    break;
                case LEFT:
                case A:
                    colMove--;
                    break;
                case Q:
                    rowMove--;
                    colMove--;
                    break;
                case E:
                    rowMove--;
                    colMove++;
                    break;
                case C:
                    rowMove++;
                    colMove++;
                    break;
                case Z:
                    rowMove++;
                    colMove--;
                    break;

            }
            this.mViewModel.KeyPressed(rowMove, colMove);
            keyEvent.consume();
        }
        else
        {
            mViewModel.mStatusUpdate.set("You cant move the player to there. Please try again and read about the game.");
        }
    }

    public void MouseDrag(MouseEvent mouseEvent){
        if(!mMazeDisplayer.isPlayerWinTheGame() && mMazeDisplayer.getmMaze()!=null) {
            lbl_status.textProperty().set("Trying to move the player.");
            try {
                if (mMazeDisplayer.getmMaze() != null) {
                    double canvasHeight = mMazeDisplayer.getHeight();
                    double canvasWidth = mMazeDisplayer.getWidth();
                    int x = (int) (canvasWidth / mMazeDisplayer.getmMaze()[0].length);
                    int y = (int) (canvasHeight / mMazeDisplayer.getmMaze().length);
                    mViewModel.MouseDrag((int) (mouseEvent.getY() / y), (int) (mouseEvent.getX() / x));
                }
            } catch (Exception e) {
                new AlertBox("Oops","Problem with mouse drag");
            }
        }
        else
        {
            mViewModel.mStatusUpdate.set("You cant drag the player to there. Please try again and read about the game.");
        }
    }

    public void solveTheMaze() {
        mViewModel.mStatusUpdate.set("Trying to solve your maze!");
        this.mViewModel.solveBoard();
    }

    public void About(ActionEvent actionEvent) {
        try {
            mViewModel.mStatusUpdate.set("Open About page");
            AboutController AC = new AboutController();
            AC.displayBoard();
        } catch (Exception e) {
            new AlertBox("Oops", "Failed to open about.");
        }
    }

    public void Controls(ActionEvent actionEvent) {
        try {
            mViewModel.mStatusUpdate.set("Open controls page");
            ControlsController CC = new ControlsController();
            CC.displayBoard();
        } catch (Exception e)
        {
            new AlertBox("Oops", "Failed to open controls.");
        }
    }

    public void Objective(ActionEvent actionEvent) {
        try{
            mViewModel.mStatusUpdate.set("Open objective page");
            ObjectiveController OC = new ObjectiveController();
            OC.displayBoard();
        }
        catch (Exception e){
            new AlertBox("Oops", "Failed to open objective");
        }
    }

    public void saveMaze() {
        if(this.mMazeDisplayer.getmMaze() != null) {
            SaveController SC = new SaveController();
            String save = SC.displaySave();
            if(save != null && save.length() != 0) {
                if (!save.equals("") && save.length() < 5) {
                    save = save + ".maze";
                } else if (!save.substring(save.length() - 5, save.length()).equals(".maze")) {
                    save = save + ".maze";
                }
                if (!save.equals(".maze")) {
                    if(this.mViewModel.saveBoard("./src/resources/Mazes/" + save)) {
                        new AlertBox("Save maze", save + " saved successfully!");
                    }
                    else{
                        new AlertBox("Save maze", "Unable to save maze - Invalid name");
                    }
                }else{
                    new AlertBox("Save maze", "Maze wasn't saved");
                }
            }
        }
        else{
            new AlertBox("Save maze", "Please generate or Load a maze first.");
        }
    }

    public void loadMaze(ActionEvent actionEvent) {
         LoadController LC = new LoadController();
        String path = LC.displayLoad();
        if (path != null && !path.equals("")) {
            if (path.length() > 5 && path.substring(path.length() - 5, path.length()).equals(".maze")) {
                path = path.substring(0, path.length() - 6);
            } else {
                path = path + ".maze";
            }
            ConfirmBoxController CB = new ConfirmBoxController();
            if (CB.confirm() == 1) {
                if (this.mViewModel.loadBoard("./src/resources/Mazes/" + path)) {
                    new AlertBox("Load Maze", "Maze loaded successfully!");
                }
                else{
                    new AlertBox("Load Maze", path+" not found.");
                }
            }
        }
    }

    public void exitProgram() {
        ConfirmBoxController CB = new ConfirmBoxController();
        int answer = CB.confirm();
        if(answer == 1){ //exit
            this.mViewModel.exit();
            mPrimaryStage.close();
        }
        else if(answer == 2){ //save and exit
            System.out.println("Save it first");
            saveMaze();
            //this.viewModel.exit();
            //primaryStage.close();
        }
    }

    public void setStage(Stage primaryStage){ //gives us a reference to the primary stage and deals with the X button
        this.mPrimaryStage = primaryStage;
        primaryStage.setOnCloseRequest( event -> {
            event.consume();
            exitProgram();
        });
    }

      public void displayProperties(ActionEvent actionEvent) {
        try{
            new PropertiesController().displayBoard();
        }
        catch (Exception e){
            new AlertBox("Oops","Failed to open properties window");
        }
    }
}
