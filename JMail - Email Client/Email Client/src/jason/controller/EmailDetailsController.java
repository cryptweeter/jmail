package jason.controller;

import jason.controller.services.MessageRendererService;
import jason.model.EmailMessageBean;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends AbstractController implements Initializable {

    public EmailDetailsController(ModelAccess modelAccess) { //creates common reference (modelAccess) between the controllers so that they can both communicate with the model
        super(modelAccess); // sets the parent of this class to be the abstract controller so that it can have access to the general model access object and share that access with the other controllers
    }

    @FXML
    private WebView webView;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label senderLabel;

    @Override
        public void initialize(URL location, ResourceBundle resources) {
        // start new message renderer service using email details window webview
        MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
        //retrieve the correct message
        EmailMessageBean selectedMessage = getModelAccess().getSelectedMessage();
        // render header
        subjectLabel.setText("Subject: " + selectedMessage.getSubject());
        senderLabel.setText("Sender: " + selectedMessage.getSender());
        // render message
        messageRendererService.setMessageToRender(selectedMessage);
        messageRendererService.restart();
    }
}
