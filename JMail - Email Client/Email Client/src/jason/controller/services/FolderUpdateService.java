// Implements a service to update our folders (inbox, sent, etc) constantly and automatically when we have new entries //

package jason.controller.services;


import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import java.util.List;

public class FolderUpdateService extends Service<Void> {

    private List<Folder> foldersList;

    public FolderUpdateService(List<Folder> foldersList) {
        this.foldersList = foldersList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (;;) {
                    try {
                        Thread.sleep(10000);
                        if (FetchFolderService.noServicesActive()) {
                            for (Folder folder : foldersList) {
                                if (folder.getType() != Folder.HOLDS_FOLDERS && folder.isOpen()) {
                                    folder.getMessageCount();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
    }
}
