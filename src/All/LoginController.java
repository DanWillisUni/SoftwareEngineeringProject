package All;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {
    @FXML private Text actiontarget;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML
    private void LoginHandleSubmitButtonAction (ActionEvent event) throws IOException {
        DatabaseController db = new DatabaseController();
        String matchingPassword = db.getMatchingPassword(email.getText());
        if(matchingPassword!=null){
            if(matchingPassword.equals(password.getText())){
                Parent RegistrationParent = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                Scene scene = new Scene(RegistrationParent);
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                actiontarget.setText("Incorrect password details");
            }
        } else {
            actiontarget.setText("Incorrect email details");
        }
    }
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) throws IOException {
        //next 5 lines changes the page
        Parent RegistrationParent = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Scene scene = new Scene(RegistrationParent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
