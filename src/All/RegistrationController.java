package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML private TextField forename;
    @FXML private TextField surname;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField password2;
    @FXML private DatePicker DOB;
    @FXML private ComboBox gender;
    @FXML private TextField height;
    @FXML private Label errorMsg;
    @FXML
    protected void RegisterHandleSubmitButtonAction(ActionEvent event) throws IOException {
        DatabaseController db = new DatabaseController();
        errorMsg.setText("");
        if (forename.getText()!=null){
            if (!forename.getText().equals("")){
                if (forename.getText().toString().length()>20){
                    errorMsg.setText("Error: forename too long");
                }
            } else {
                errorMsg.setText("Error: forename null");
            }
        } else {
            errorMsg.setText("Error: forename null");
        }
        if (surname.getText()!=null){
            if (!surname.getText().equals("")){
                if (surname.getText().toString().length()>20){
                    errorMsg.setText("Error: surname too long");
                }
            } else {
                errorMsg.setText("Error: surname null");
            }
        } else {
            errorMsg.setText("Error: surname null");
        }
        if (username.getText()!=null){
            if (!username.getText().equals("")){
                if (username.getText().toString().length()>20){
                    errorMsg.setText("Error: username too long");
                } else {
                    if(!db.isStr(username.getText(),"personalinfo","username")){
                        errorMsg.setText("Error: username already in use");
                    }
                }
            }else{
                errorMsg.setText("Error: username null");
            }
        }else{
            errorMsg.setText("Error: username null");
        }
        if (email.getText()!=null){
            if (!email.getText().equals("")){
                if (email.getText().toString().length()<20){
                    if (email.getText().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                        if(!db.isStr(email.getText(),"personalinfo","email")){
                            errorMsg.setText("Error: email already in use");
                        }
                    } else {
                        errorMsg.setText("Error: Not a valid email");
                    }
                } else {
                    errorMsg.setText("Error: email too long");
                }
            }else {
                errorMsg.setText("Error: email null");
            }
        } else {
            errorMsg.setText("Error: email null");
        }
        if (password.getText()!=null){
            if (!password.getText().equals("")){
                if (password.getText().toString().length()>20){
                    errorMsg.setText("Error: password too long");
                }
            } else {
                errorMsg.setText("Error: password null");
            }
        }else{
            errorMsg.setText("Error: password null");
        }
        if (password2.getText()!=null){
            if (!password2.getText().equals("")){
                if (password2.getText().toString().length()>20){
                    errorMsg.setText("Error: password2 too long");
                }
            }else {
                errorMsg.setText("Error: password2 null");
            }
        } else {
            errorMsg.setText("Error: password2 null");
        }
        if(DOB.getValue()!=null){
            Long d = Date.from(Instant.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()))).getTime();
            Long c = new Date().getTime();
            if (c>d){
                if (c - 3153600000000L > d){
                    errorMsg.setText("Error: date to long ago");
                }
            } else {
                errorMsg.setText("Error: date in the future");
            }
        }else {
            errorMsg.setText("Error: Date not selected");
        }
        if (gender.getValue()==null) {
            errorMsg.setText("Error: gender not selected");
        }else if(gender.getValue().toString().equals("")){
            errorMsg.setText("Error: gender not typed in");
        } else {
            if(!gender.getValue().toString().equals("Male")&&!gender.getValue().toString().equals("Female")&&!gender.getValue().toString().equals("Other")){
                errorMsg.setText("Error: not valid gender");
            }
        }
        if (height.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            int i = Integer.parseInt(height.getText());
            if (i>0){
                if (i>250){
                    errorMsg.setText("Error: height greater than 250");
                }
            } else {
                errorMsg.setText("Error: height negative");
            }
        } else {
            errorMsg.setText("Error: height not numeric");
        }
        if (!password.getText().equals(password2.getText())) {
            errorMsg.setText("Passwords do not match");
        }
        if (errorMsg.getText().equals("")){
            Person newPerson = new Person(forename.getText(),surname.getText(),username.getText(),email.getText(),password.getText(), Date.from(Instant.from(DOB.getValue().atStartOfDay(ZoneId.systemDefault()))),new BigDecimal(height.getText()), gender.getValue().toString().charAt(0));
            db.addUser(newPerson);
            Parent RegistrationParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(RegistrationParent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

    }
    @FXML
    private void GoToLoginButtonAction (ActionEvent event) throws IOException {
        //next 5 lines changes the page
        Parent RegistrationParent = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(RegistrationParent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
