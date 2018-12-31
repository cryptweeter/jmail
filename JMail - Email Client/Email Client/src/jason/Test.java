package jason;

import jason.model.EmailAccountBean;
import jason.model.EmailMessageBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Test {

    public static void main (String[] args) {
        final EmailAccountBean emailAccountBean = new EmailAccountBean("jasontest797@gmail.com","randomcat123");

        ObservableList<EmailMessageBean> data = FXCollections.observableArrayList();

    }

}
