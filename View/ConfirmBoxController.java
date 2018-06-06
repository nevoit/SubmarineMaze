package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * Created by Tomash on 24-Jun-17.
 */

public class ConfirmBoxController{

    @FXML
    public javafx.scene.control.Button BTN_save;
    @FXML
    public Label label_Message;
    @FXML
    public javafx.scene.control.Button yes_BTN;
    @FXML
    public javafx.scene.control.Button no_BTN;

    private Stage stage;
    private static int answer; //0 - no, 1 - yes, 2 - save

    public int confirm(){
        try{
            stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("ConfirmBox.fxml").openStream());
            root.getStylesheets().add(getClass().getResource("ConfirmStyle.css").toExternalForm());
            Font.loadFont(getClass().getResourceAsStream("../resources/Fonts/Roboto-Black.ttf"),15);
            Scene scene = new Scene(root, 200, 90);
            stage.setTitle("Please Confirm");
            scene.getStylesheets().add(getClass().getResource("ConfirmStyle.css").toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        catch(Exception E){
            new AlertBox("Oops!","An error occurred.");
            return 0;
        }
            stage.showAndWait();
            return answer;
    }

    public void saveMaze(ActionEvent actionEvent) {
        this.answer = 2;
        Stage stage = (Stage) yes_BTN.getScene().getWindow();
        stage.close();
    }

    public void yes() {
        this.answer = 1;
        Stage stage = (Stage) yes_BTN.getScene().getWindow();
        stage.close();
    }

    public void no() {
        this.answer = 0;
        Stage stage = (Stage) no_BTN.getScene().getWindow();
        stage.close();
    }
}
