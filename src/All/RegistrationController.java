package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.ZoneId;

public class RegistrationController {
    @FXML private Text actiontarget;
    @FXML private TextField forename;
    @FXML private TextField surname;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField password2;
    @FXML private DatePicker DOB;
    @FXML private ComboBox gender;
    @FXML private TextField height;
    @FXML
    protected void RegisterHandleSubmitButtonAction(ActionEvent event){
        //get all the things and get a person from it
        //validate everything
        if (password.getText().equals(password2.getText())){
            Person newPerson = new Person(forename.getText(),surname.getText(),username.getText(),email.getText(),password.getText(),new java.sql.Date(java.util.Date.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime()),new BigDecimal(height.getText()));
            DatabaseController db = new DatabaseController();
            db.addUser(newPerson);
        } else {
            actiontarget.setText("Passwords do not match");
        }

    }
}
