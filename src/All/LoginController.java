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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private Button SignIn,Register;
    @FXML private Text actiontarget;
    @FXML
    private void LoginHandleSubmitButtonAction (ActionEvent event) {
        //login if details are correct
        actiontarget.setText("Incorrect login details");
        //go to dashboard
    }
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) throws IOException {
        Parent RegistrationParent = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Scene scene = new Scene(RegistrationParent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
