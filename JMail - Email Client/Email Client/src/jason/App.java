package jason;

import jason.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        ViewFactory viewFactory = ViewFactory.defaultViewFactory;

        Scene scene = viewFactory.getMainScene();

        primaryStage.setScene(scene);
        primaryStage.show();


    }

}