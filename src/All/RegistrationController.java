package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class RegistrationController {
    @FXML private Text actiontarget;
    @FXML
    protected void RegisterHandleSubmitButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        actiontarget.setText("Register button pressed");
        //get all the things and get a person from it
        Person newPerson = new Person();
        DatabaseController db = new DatabaseController("RegistrationController","@localhost","SoftwareEngineering","SoftwareEngineering");
        db.addPerson(newPerson);
    }
}
