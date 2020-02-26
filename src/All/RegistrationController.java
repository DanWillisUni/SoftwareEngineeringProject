package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    protected void RegisterHandleSubmitButtonAction(ActionEvent event) throws IOException {
        //get all the things and get a person from it
        //validate everything
        //check emails
        //sort out dates
        if (password.getText().equals(password2.getText())){
            Person newPerson = new Person(forename.getText(),surname.getText(),username.getText(),email.getText(),password.getText(), Date.from(Instant.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()))),new BigDecimal(height.getText()));
            DatabaseController db = new DatabaseController();
            db.addUser(newPerson);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            DashboardController controller = loader.<DashboardController>getController();
            controller.setUser(newPerson);
            controller.setUpDisplay();
            stage.show();
        } else {
            actiontarget.setText("Passwords do not match");
        }
    }
}
