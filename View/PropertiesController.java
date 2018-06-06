package View;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Tomash on 24-Jun-17.
 */
public class PropertiesController implements IView {


    public void displayBoard(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Game Properties");
        Properties prop = new Properties();
        Pane P = new Pane();
        Scene scene = new Scene(P);
        try {
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);
            String configurations = "";
            Enumeration<?> enumeration = prop.propertyNames();
            while(enumeration.hasMoreElements()){
                String var = (String) enumeration.nextElement();
                String val = prop.getProperty(var);
                configurations += var+": "+val+"\n";
            }
            Label L = new Label(configurations);
            P.getChildren().addAll(L);
        } catch (FileNotFoundException e1){
            new AlertBox("Oops", "Failed to open properties file.");
        } catch (IOException e) {
            new AlertBox("Oops","Failed to read file.");
        }
        stage.setMinHeight(130);
        stage.setMinWidth(290);
        stage.setScene(scene);
        stage.show();
    }
}
