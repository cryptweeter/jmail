package jason.controller.services;


import jason.controller.ModelAccess;
import jason.model.EmailAccountBean;
import jason.model.EmailConstants;
import jason.model.folder.EmailFolderBean;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CreateAndRegisterEmailAccountService extends Service<Integer> {

    private String emailAddress;
    private String password;
    private EmailFolderBean<String> folderRoot;
    private ModelAccess modelAccess;

    public CreateAndRegisterEmailAccountService(String emailAddress, String password, EmailFolderBean<String> folderRoot, ModelAccess modelAccess) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.folderRoot = folderRoot;
        this.modelAccess = modelAccess;
    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>(){
            @Override
            protected Integer call() throws Exception {
                EmailAccountBean emailAccount = new EmailAccountBean(emailAddress, password);
                if (emailAccount.getLoginState() == EmailConstants.LOGIN_STATE_SUCCEEDED) {
                    modelAccess.addAccount(emailAccount);
                    EmailFolderBean<String> emailFolderBean = new EmailFolderBean<String>(emailAddress);
                    folderRoot.getChildren().add(emailFolderBean);
                    FetchFolderService fetchFolderService = new FetchFolderService(emailFolderBean, emailAccount, modelAccess);
                    fetchFolderService.start();
                }
                return emailAccount.getLoginState();
            }
        };
    }

}
