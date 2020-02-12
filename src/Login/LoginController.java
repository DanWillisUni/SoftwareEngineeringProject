package Login;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
    @FXML private Button SignIn,Register;
    @FXML private Text actiontarget;
    @FXML
    private void LoginHandleSubmitButtonAction (ActionEvent event) throws Exception {
        //login if details are correct
        actiontarget.setText("Incorrect login details");
        //go to dashboard
    }
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registration\\Registration.fxml"));
        Stage stage = (Stage) Register.getScene().getWindow();
        Scene scene = new Scene(loader.getRoot());
        stage.setScene(scene);
    }
}
