package jason.model.folder;

import jason.model.EmailMessageBean;
import jason.view.ViewFactory;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;

public class EmailFolderBean<T> extends TreeItem<String> {

    private boolean topElement = false;
    private int unreadMessageCount;
    private String name;
    private String completeName;
    private ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();

    //Constructor for top elements
    public EmailFolderBean(String value){
        super(value, ViewFactory.defaultViewFactory.resolveIcon(value));
        this.name = value;
        this.completeName = value;
        data = null;
        topElement = true;
        this.setExpanded(true);
    }

    public EmailFolderBean(String value, String completeName){
        super(value, ViewFactory.defaultViewFactory.resolveIcon(value));
        this.name = value;
        this.completeName = completeName;
    }

    private void updateValue(){
        if (unreadMessageCount > 0) {
            this. setValue((String)(name + " (" + unreadMessageCount + ")"));
        } else {
            this.setValue(name);
        }
    }

    public void incrementUnreadMessagesCount(int newMessages){
        unreadMessageCount += newMessages;
        updateValue();
    }

    public void decrementUnreadMessagesCount(){
        unreadMessageCount--;
        updateValue();
    }

    public void addEmail(int position, Message message) throws MessagingException {
        boolean isRead = message.getFlags().contains(Flags.Flag.SEEN);
        EmailMessageBean emailMessageBean = new EmailMessageBean(message.getSubject(), message.getFrom()[0].toString(), message.getSize(), isRead, message.getSentDate(), message);
        if (position < 0) {
            data.add(emailMessageBean);
        } else {
            data.add(position, emailMessageBean);
        }
        if (!isRead) {
            incrementUnreadMessagesCount(1);
        }

    }

    public boolean isTopElement(){
        return topElement;
    }

    public ObservableList<EmailMessageBean> getData(){
        return data;
    }

}
