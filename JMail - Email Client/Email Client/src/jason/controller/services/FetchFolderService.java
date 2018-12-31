// Implements a service to fetch our email folders e.g. inbox, sent, etc and their content from the gmail servers //


package jason.controller.services;

import jason.controller.ModelAccess;
import jason.model.EmailAccountBean;
import jason.model.folder.EmailFolderBean;
import javafx.concurrent.Task;
import javafx.concurrent.Service;


import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;

public class FetchFolderService extends Service<Void> {

    private EmailFolderBean<String> foldersRoot;
    private EmailAccountBean emailAccountBean;
    private ModelAccess modelAccess;
    private static int NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE = 0;

    public FetchFolderService(EmailFolderBean<String> foldersRoot, EmailAccountBean emailAccountBean, ModelAccess modelAccess) {
        this.foldersRoot = foldersRoot;
        this.emailAccountBean = emailAccountBean;
        this.modelAccess = modelAccess;

        this.setOnSucceeded(e -> {
            NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE--; // Makes number of services active 0 so that we know its OK to start iterating over number of folders and don't get a concurrent modification error
        });
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE++; // tracks if a service is active
                if (emailAccountBean != null) {
                    Folder[] folders = emailAccountBean.getStore().getDefaultFolder().list();

                    for (Folder folder:folders) { // an 'enhanced' for loop aka a 'for-each' loop. loops over arrays.
                        modelAccess.addFolder(folder);
                        EmailFolderBean<String> item = new EmailFolderBean<String>(folder.getName(), folder.getFullName());
                        foldersRoot.getChildren().add(item);
                        item.setExpanded(true);
                        addMessageListenerToFolder(folder, item);
                        FetchMessagesInFolderService fetchMessagesInFolderService = new FetchMessagesInFolderService(item, folder);
                        fetchMessagesInFolderService.start();
                        System.out.println("added: " + folder.getName());

                        Folder[] subFolders = folder.list();
                        for (Folder subFolder: subFolders) {
                            modelAccess.addFolder(subFolder);
                            EmailFolderBean<String> subItem = new EmailFolderBean<String>(subFolder.getName(), subFolder.getFullName());
                            item.getChildren().add(subItem);
                            addMessageListenerToFolder(subFolder, subItem);
                            FetchMessagesInFolderService fetchMessagesInSubFolderService = new FetchMessagesInFolderService(subItem, subFolder);
                            fetchMessagesInSubFolderService.start();
                            System.out.println("added: " + subFolder.getName());
                        }
                    }
                }

                return null;

            }
        };
    }

    private void addMessageListenerToFolder (Folder folder, EmailFolderBean<String> item) {
        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                for (int i = 0; i < e.getMessages().length; i++) {
                    try {
                        Message currentMessage = folder.getMessage(folder.getMessageCount() - i);
                        item.addEmail(0, currentMessage);
                    } catch (MessagingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public static boolean noServicesActive() {
        return NUMBER_OF_FETCHFOLDERSERVICES_ACTIVE == 0;
    }

}
