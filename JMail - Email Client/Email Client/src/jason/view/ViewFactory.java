package jason.view;

import jason.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;

public class ViewFactory {

    public static ViewFactory defaultViewFactory = new ViewFactory();
    private static boolean mainViewInitialized = false;

    private final String DEFAULT_CSS = "style.css";
    private final String EMAIL_DETAILS_FXML = "EmailDetailsLayout.fxml";
    private final String MAIN_LAYOUT_FXML = "MainLayout.fxml";
    private final String MAIN_SCREEN_FXML = "MainLayout.fxml";
    private final String COMPOSE_EMAIL_FXML = "ComposeMessageLayout.fxml";

    private ModelAccess modelAccess = new ModelAccess();

    private MainController mainController;
    private EmailDetailsController emailDetailsController;


    public Scene getMainScene() throws OperationNotSupportedException {
        if (!mainViewInitialized) {
            mainController = new MainController(modelAccess);
            mainViewInitialized = true;
            return initializeScene(MAIN_SCREEN_FXML, mainController);
        } else{
            throw new OperationNotSupportedException("Main Scene already initialised");
        }

    }


    public Scene getEmailDetailsScene(){
        emailDetailsController = new EmailDetailsController(modelAccess);
        return initializeScene(EMAIL_DETAILS_FXML, emailDetailsController);
    }

    public Scene getComposeMessageScene() {
        AbstractController composeController = new ComposeMessageController(modelAccess);
        return initializeScene(COMPOSE_EMAIL_FXML, composeController);
    }

    // Method to set graphics (icons) in the treeview
    public Node resolveIcon(String treeItemValue) {
        String lowerCaseTreeItemValue = treeItemValue.toLowerCase();
        ImageView returnIcon;
        try {
            if (lowerCaseTreeItemValue.contains("inbox")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/inbox.png")));
            } else if (lowerCaseTreeItemValue.contains("sent")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/sent.png")));
            } else if (lowerCaseTreeItemValue.contains("spam")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/spam.png")));
            } else if (lowerCaseTreeItemValue.contains("bin")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/deleted.png")));
            } else if (lowerCaseTreeItemValue.contains("@")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/email.png")));
            } else if (lowerCaseTreeItemValue.contains("important")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/important.png")));
            } else if (lowerCaseTreeItemValue.contains("draft")) {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/draft.png")));
            } else {
                returnIcon = new ImageView(new Image(getClass().getResourceAsStream("../view/images/folder.png")));
            }
        } catch (NullPointerException e) {
            System.out.println("Invalid image location");
            e.printStackTrace();
            returnIcon = new ImageView();
        }
        returnIcon.setFitHeight(16);
        returnIcon.setFitWidth(16);

        return returnIcon;
    }

    private Scene initializeScene(String fxmlPath, AbstractController controller){
        FXMLLoader loader;
        Parent parent;
        Scene scene;

        try {
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setController(controller);
            parent = loader.load();
        } catch (Exception e) {
            return null;
        }

        scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource(DEFAULT_CSS).toExternalForm());
        return scene;
    }


}
