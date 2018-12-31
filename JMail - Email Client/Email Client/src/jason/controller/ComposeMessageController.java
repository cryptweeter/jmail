package jason.controller;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import jason.controller.services.EmailSenderService;
import jason.model.EmailConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javafx.fxml.Initializable;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ComposeMessageController extends AbstractController implements Initializable {

    private List<File> attachments = new ArrayList<File>();

    @FXML
    private Label attachmentsLabel;

    @FXML
    private JFXTextField recipientField;

    @FXML
    private JFXTextField subjectField;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<String> senderChoice;

    @FXML
    private HTMLEditor contentField;

    @FXML
    void attachBtnAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            attachments.add(selectedFile);
            attachmentsLabel.setText(attachmentsLabel.getText() + selectedFile.getName() + "; ");
        }
    }

    @FXML
    void sendBtnAction(ActionEvent event) {
        errorLabel.setText("");
        EmailSenderService emailSenderService = new EmailSenderService(getModelAccess().getEmailAccountByName(senderChoice.getValue()), subjectField.getText(), recipientField.getText(), contentField.getHtmlText(), attachments);
        emailSenderService.restart();
        emailSenderService.setOnSucceeded(e -> {
            if (emailSenderService.getValue() == EmailConstants.MESSAGE_SENT_OK) {
                errorLabel.setText("Message sent!");
            } else {
                errorLabel.setText("Error when sending message.");
            }
        });
    }

    public ComposeMessageController(ModelAccess modelAccess) {
        super(modelAccess);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        senderChoice.setItems(getModelAccess().getEmailAccountNames());
        senderChoice.setValue(getModelAccess().getEmailAccountNames().get(0));
    }


}
