package View;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.*;
import javafx.scene.text.Text;


/**
 * Created by Tomash on 23-Jun-17.
 */
public class SaveController{

    public Button save_BTN;
    public Button cancel_BTN;
    public javafx.scene.control.TextField textfield;

    private static String toSave;
    private Stage stage;

  //  private ActionEvent actionEvent;

    public SaveController(){
        stage = new Stage();
    }

    public String displaySave() {
        try{
            stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Save.fxml").openStream());
            Scene scene = new Scene(root,250,140);
            stage.setTitle("Save Maze");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
          /*  stage.setOnCloseRequest( event -> {
                event.consume();
                cancel();
            }); */
            stage.showAndWait();
            return toSave;
        }
        catch(Exception E){
            new AlertBox("Oops","Failed to save.");
            return "";
        }
    }

    public void saveBTN(){
        toSave = textfield.getText();
        if(toSave.equals("")){
            new AlertBox("Oops!","You didn't enter a file name.");
        }
        else{
            Stage stage = (Stage) save_BTN.getScene().getWindow();
            stage.close();
        }
    }

    public void cancel(){
        toSave = "";
        Stage stage = (Stage) cancel_BTN.getScene().getWindow();
        stage.close();
    }

}
