package jason.model;

import javafx.collections.ObservableList;

import javax.mail.*;
import java.util.Properties;

public class EmailAccountBean {

    private String emailAddress;
    private String password;
    private Properties properties;

    private Store store;
    private Session session;
    private int loginState = EmailConstants.LOGIN_STATE_NOT_READY;

    public String getEmailAddress() {
        return emailAddress;
    }

    public Properties getProperties() {
        return properties;
    }

    public Store getStore() {
        return store;
    }

    public Session getSession() {
        return session;
    }

    public int getLoginState() {
        return loginState;
    }

    public EmailAccountBean(String EmailAddress, String Password){
        this.emailAddress = EmailAddress;
        this.password = Password;

        properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.smtps.protocol", "smtp.gmail.com");
        properties.put("mail.smtps.protocol", "true");
        properties.put("incomingHost", "imap.gmail.com");
        properties.put("outgoingHost", "smtp.gmail.com");

        Authenticator auth = new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(EmailAddress, password);
            }
        };

        // Connecting to Gmail's email servers
        session = Session.getInstance(properties, auth);
        try {
            this.store = session.getStore();
            store.connect(properties.getProperty("incomingHost"), emailAddress, password);
            System.out.println("EmailAccountBean successfully constructed");
            loginState = EmailConstants.LOGIN_STATE_SUCCEEDED;
        } catch (Exception e) {
            loginState = EmailConstants.LOGIN_STATE_FAILED_BY_CREDENTIALS;
        }
    }

    public String getPassword() {
        return password;
    }
}
