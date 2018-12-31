package jason.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTreeView;
import jason.controller.services.CreateAndRegisterEmailAccountService;
import jason.controller.services.FolderUpdateService;
import jason.controller.services.MessageRendererService;
import jason.controller.services.SaveAttachmentsService;
import jason.model.EmailAccountBean;
import jason.model.EmailMessageBean;
import jason.model.folder.EmailFolderBean;
import jason.model.table.BoldableRowFactory;
import jason.model.table.FormattableInteger;
import jason.view.ViewFactory;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.text.Element;
import javax.swing.text.View;

public class MainController extends AbstractController implements Initializable{

    public MainController(ModelAccess modelAccess) { //creates common reference (modelAccess) between the controllers so that they can both communicate with the model
        super(modelAccess); // sets the parent of this class to be the abstract controller so that it can have access to the general model access object and share that access with the other controllers // essentially says this.modelAcess = AbstractTableItem.modelAccess
    }

    @FXML
    private JFXTreeView<String> emailFoldersTreeView;
    private MenuItem showDetails = new MenuItem("Show details");

    @FXML
    private TableView<EmailMessageBean> emailTableView;

    @FXML
    private TableColumn<EmailMessageBean, String> subjectCol;

    @FXML
    private TableColumn<EmailMessageBean, Date> dateCol;

    @FXML
    private TableColumn<EmailMessageBean, String> senderCol;

    @FXML
    private TableColumn<EmailMessageBean, FormattableInteger> sizeCol;

    @FXML
    private WebView messageRenderer;


    @FXML
    private JFXButton downAttachBtn;

    private SaveAttachmentsService saveAttachmentsService;

    @FXML
    private JFXProgressBar downAttachProgress;


    @FXML
    private JFXButton composeBtn;

    private MessageRendererService messageRendererService;


    @FXML
    void composeBtnAction(ActionEvent event) {
        Scene scene = ViewFactory.defaultViewFactory.getComposeMessageScene();
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void downAttachBtnAction(ActionEvent event) {
        EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
        if (message != null  && message.hasAttachments()){
            saveAttachmentsService.setMessageToDownload(message);
            saveAttachmentsService.restart();
            if (saveAttachmentsService.isRunning()) {
                System.out.println("Saving attachments...");
            }
       }
    }


//    @FXML
//    void changeReadAction() {
//        EmailMessageBean message = getModelAccess().getSelectedMessage();
//        if (message != null) {
//            boolean value = message.isRead();
//            message.setRead(!value);
//            EmailFolderBean<String> selectedFolder = getModelAccess().getSelectedFolder();
//            if (selectedFolder != null) {
//                if (value) {
//                    selectedFolder.incrementUnreadMessagesCount(1);
//                } else {
//                    selectedFolder.decrementUnreadMessagesCount();
//                }
//            }
//        }
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        downAttachProgress.setVisible(false);
        saveAttachmentsService = new SaveAttachmentsService(downAttachProgress);
        messageRendererService = new MessageRendererService(messageRenderer.getEngine());


        FolderUpdateService folderUpdateService = new FolderUpdateService(getModelAccess().getFoldersList());
        folderUpdateService.start();

        emailTableView.setRowFactory(e -> new BoldableRowFactory<>());
        ViewFactory viewFactory = ViewFactory.defaultViewFactory;

        subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("subject"));
        senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, String>("sender"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, Date>("date"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessageBean, FormattableInteger>("size"));

        sizeCol.setComparator(new FormattableInteger(0)); // Overrides default comparator to prevent bug. Used to properly sort size column.

        EmailFolderBean<String> root = new EmailFolderBean<>("");
        emailFoldersTreeView.setRoot(root);
        emailFoldersTreeView.setShowRoot(false); // because the topmost (the real root) should be invisible to still look good when there's multiple emails in the client

        CreateAndRegisterEmailAccountService createAndRegisterEmailAccountService = new CreateAndRegisterEmailAccountService("jasontest797@gmail.com","randomcat123", root, getModelAccess());
        createAndRegisterEmailAccountService.start();

        emailTableView.setContextMenu(new ContextMenu(showDetails));

        // Allows clicking on different folders to change which emails you see
        emailFoldersTreeView.setOnMouseClicked(e -> { // Consider making this block into a function called renderEmails and put it in the view folder and just call renderEmails here
            EmailFolderBean<String> item = (EmailFolderBean<String>)emailFoldersTreeView.getSelectionModel().getSelectedItem(); // item = inbox or sent or junk or deleted
            if (item != null && !item.isTopElement()) {
                emailTableView.setItems(item.getData()); // puts the data (i.e. the emails) of the selected item (inbox, sent etc) into the tableview
                getModelAccess().setSelectedFolder(item);
                getModelAccess().setSelectedMessage(null); //clears the selected message after you select a new folder
            }
        });
        // Shows message in main window when you click on an email
        emailTableView.setOnMouseClicked(e -> {
            EmailMessageBean message = emailTableView.getSelectionModel().getSelectedItem();
            if (message != null) {
                getModelAccess().setSelectedMessage(message);
                messageRendererService.setMessageToRender(message);
                messageRendererService.restart();
            }
        });

        showDetails.setOnAction(e -> {

            Scene scene = viewFactory.getEmailDetailsScene();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        });


    }

}
