package jason.controller;

import jason.model.EmailAccountBean;
import jason.model.EmailMessageBean;
import jason.model.folder.EmailFolderBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.mail.Folder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelAccess {

    private Map<String, EmailAccountBean> emailAccounts = new HashMap<String, EmailAccountBean>();
    private ObservableList<String> emailAccountNames = FXCollections.observableArrayList();

    public ObservableList<String> getEmailAccountNames() {
        return emailAccountNames;
    }

    public EmailAccountBean getEmailAccountByName(String name) {
        return emailAccounts.get(name);
    }

    public void addAccount(EmailAccountBean account) {
        emailAccounts.put(account.getEmailAddress(), account);
        emailAccountNames.add(account.getEmailAddress());
    }

    private EmailMessageBean selectedMessage;

    public EmailMessageBean getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessageBean selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    private EmailFolderBean<String> selectedFolder;

    public EmailFolderBean<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailFolderBean<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    private List<Folder> foldersList = new ArrayList<Folder>();

    public List<Folder> getFoldersList() {
        return foldersList;
    }

    public void addFolder(Folder folder) {
        foldersList.add(folder);
    }

}