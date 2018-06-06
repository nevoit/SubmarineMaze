package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Created by Tomash on 22-Jun-17.
 */

public class ControlsController implements  IView{


    public void displayBoard(){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Controls.fxml").openStream());
            root.getStylesheets().add(getClass().getResource("ControlsStyle.css").toExternalForm());
            Font.loadFont(getClass().getResourceAsStream("../resources/Fonts/BNMachine.ttf"),15);
            Scene scene = new Scene(root,235,200);
            scene.getStylesheets().add(getClass().getResource("ControlsStyle.css").toExternalForm());
            stage.setTitle("Controls");
            stage.setScene(scene);
            stage.setMinHeight(480);
            stage.setMinWidth(580);
            stage.setResizable(false);
          //  stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        catch(Exception E){
            E.printStackTrace();
        }
    }
}

