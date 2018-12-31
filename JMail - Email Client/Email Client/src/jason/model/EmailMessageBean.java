package jason.model;

import jason.model.table.AbstractTableItem;
import jason.model.table.FormattableInteger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.*;

public class EmailMessageBean extends AbstractTableItem {

    @Override
    public String toString() {
        return "EmailMessageBean{" +
                "sender=" + sender.get() +
                ", subject=" + subject.get() +
                ", size=" + size.get();
    }

    private SimpleStringProperty sender;
    private SimpleStringProperty subject;
    private SimpleObjectProperty<FormattableInteger> size;
    private Message messageReference;
    private SimpleObjectProperty<Date> date;

    private List<MimeBodyPart> attachmentsList = new ArrayList<MimeBodyPart>();
    private StringBuffer attachmentNames = new StringBuffer();



    public EmailMessageBean(String Subject, String Sender, int size, boolean isRead, Date date, Message messageReference){
        super(isRead); // essentially says this.isRead = AbstractTableItem.isRead
        this.subject = new SimpleStringProperty(Subject);
        this.sender = new SimpleStringProperty(Sender);
        this.size = new SimpleObjectProperty<FormattableInteger>(new FormattableInteger(size));
        this.date = new SimpleObjectProperty<Date>(date);
        this.messageReference = messageReference;
    }

    public String getSender(){
        return sender.get();
    }
    public String getSubject(){
        return subject.get();
    }
    public FormattableInteger getSize(){
        return size.get();
    }
    public Date getDate(){
        return date.get();
    }


    public Message getMessageReference() {
        return messageReference;
    }

    public List<MimeBodyPart> getAttachmentsList() {
        return attachmentsList;
    }

    public String getAttachmentNames() {
        return attachmentNames.toString();
    }

    public void addAttachment(MimeBodyPart mbp){
        attachmentsList.add(mbp);

        try {
            attachmentNames.append(mbp.getFileName() + "; ");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean hasAttachments() {
        return attachmentsList.size() > 0;
    }

    public void clearAttachments() {
        attachmentsList.clear();
        attachmentNames.setLength(0);
    }

}
