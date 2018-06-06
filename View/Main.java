package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.ServerSocket;

public class Main extends Application {



    /**
     *
     +------------------------------------------------------------------------------------+
     |    +--Data Binding----------->   +------------------+                              |
     |    |                             |                  |                              |
     |    |                             |  MyViewModel     |   <------Notifications---+   |
     |    |     +---Notifications-------+                  |          (Events)        |   |
     |    |     |   (Events)            |                  |                          |   |
     |    v     v                       +------------------+                          |   |
     |                                    ^  |                                        |   |
     |  +------------+                    |  |                          +--------------+  |
     |  |            |                    |  |Commands                  |              |  |
     |  |  Maze      |                    |  |                          |  MyModel     |  |
     |  |  Displayer +---Commands---------+  |                          |              |  |
     |  |            |                       |                          |              |  |
     |  +------------+                       |    +------------+        +--------------+  |
     |                                       |    |            |               |          |
     |                                       |    |  IModel    |               |          |
     |                                       +--> |            |  <--Implements+          |
     |                                            +------------+                          |
     +------------------------------------------------------------------------------------+
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        ServerSocket S = new ServerSocket(11212);

        MyModel myModel = new MyModel(); //Model
        MyViewModel viewModel=new MyViewModel(myModel); //MyViewController-Model
        myModel.addObserver(viewModel);

        //-----------------

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        primaryStage.setTitle("Underwater Maze Game");
        Scene scene = new Scene(root,700,600);
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(500);
        primaryStage.getIcons().add(new Image("file:./src/resources/images/submarine2/goal32X32.png"));
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);

        //----------

        MyViewController view = fxmlLoader.getController(); //MyViewController
        view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        view.setStage(primaryStage);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
