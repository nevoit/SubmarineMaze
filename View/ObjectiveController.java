package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Tomash on 24-Jun-17.
 */
public class ObjectiveController {

    public void displayBoard(){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Objective.fxml").openStream());
            root.getStylesheets().add(getClass().getResource("ObjectiveStyle.css").toExternalForm());
            Scene scene = new Scene(root,200,140);
            scene.getStylesheets().add(getClass().getResource("ObjectiveStyle.css").toExternalForm());
            Font.loadFont(getClass().getResourceAsStream("../resources/Fonts/BNMachine.ttf"),15);
            stage.setTitle("Objective of the game");
            stage.setScene(scene);
            stage.setMinHeight(600);
            stage.setMinWidth(600);
           // stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch(Exception E){
            new AlertBox("Oops!","An error occurred.");
        }
    }
}
