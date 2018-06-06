package View;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Created by User on 18-Jun-17.
 */
public class AboutController implements  IView{


    public void displayBoard(){
        try{
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root,235,300);
            scene.getStylesheets().add(getClass().getResource("AboutStyle.css").toExternalForm());
            root.getStylesheets().add(getClass().getResource("AboutStyle.css").toExternalForm());
            Font.loadFont(getClass().getResourceAsStream("../resources/Fonts/BNMachine.ttf"),15);
            stage.setTitle("About Us");
            stage.setScene(scene);
            stage.setMinHeight(440);
            stage.setMinWidth(580);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        catch(Exception E){E.printStackTrace();}
    }
}
