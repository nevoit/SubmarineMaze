package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Created by Tomash on 23-Jun-17.
 */
public class LoadController{

    public javafx.scene.control.Button load_BTN;
    public javafx.scene.control.Button cancel_BTN;
    public javafx.scene.control.TextField pathWayText;

    private static String toLoad;
    private Stage stage;

    //  private ActionEvent actionEvent;

    public LoadController(){
        stage = new Stage();
    }

    public String displayLoad() {
        try{
            stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Load.fxml").openStream());
            Scene scene = new Scene(root,350,140);
            stage.setTitle("Load Maze");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            /*stage.setOnCloseRequest( event -> {
                event.consume();
                cancel();
            }); */
            stage.showAndWait();
        }
        catch(Exception E){
           new AlertBox("Oops!","Failed to load maze.");
        }
        return toLoad;
    }

    public void load(){
        toLoad = pathWayText.getText();
        if(toLoad.equals(""))
            new AlertBox("Load Maze","Please enter a file name.");
        else {
            Stage stage = (Stage) load_BTN.getScene().getWindow();
            stage.close();
        }
    }

    public void cancel(){
        toLoad = "";
        Stage stage = (Stage) cancel_BTN.getScene().getWindow();
        stage.close();
    }

}
