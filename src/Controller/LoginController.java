package Controller;

import Model.DatabaseController;
import Model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {
    @FXML private Label errorMsg;
    @FXML private TextField email;
    @FXML private PasswordField password;
    /**
     * if email and password valid
     * begin to load dashboard
     * from the email get the id
     * fetch all the info from database
     * create new Person object
     * set the user on dashboard to the new Person
     * @param event login button pressed
     */
    @FXML
    private void LoginHandleSubmitButtonAction (ActionEvent event) {
        DatabaseController db = new DatabaseController();
        errorMsg.setText("");
        //validation
        String matchingPassword = db.getMatchingPassword(email.getText());
        if(matchingPassword!=null){
            if(matchingPassword.equals(password.getText())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Dashboard.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                DashboardController controller = loader.<DashboardController>getController();
                db = new DatabaseController();
                int id =  db.getIDFromName(email.getText(),"personalinfo","email","idUser");
                Person u = db.getAllPersonalInfo(id);
                db.removeOverdueGoals(u.getID());
                controller.setUser(u);
                controller.setUpDisplay();
                stage.show();
            } else {
                errorMsg.setText("Incorrect password details");
            }
        } else {
            errorMsg.setText("Incorrect email details");
        }
    }
    /**
     * go to registration page
     * @param event go to registration
     */
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) {
        Parent RegistrationParent = null;
        try {
            RegistrationParent = FXMLLoader.load(getClass().getResource("../View/Registration.fxml"));//set the parent to the registration page
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(RegistrationParent);//create new scene of the registration page
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();//set the stage to be the current window
        stage.setScene(scene);//put the scene on the current window
        stage.show();//show the new page
    }
}
