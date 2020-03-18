package Controller;

import Model.DatabaseController;
import Model.Person;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

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
                db = new DatabaseController();
                int id =  db.getIDFromName(email.getText(),"personalinfo","email","idUser");
                Person u = db.getAllPersonalInfo(id);
                GenericController.goToDash(u,event);
            } else {
                errorMsg.setText("Incorrect password details");
                password.setText("");
            }
        } else {
            errorMsg.setText("Incorrect email details");
            password.setText("");
            email.setText("");
        }
    }
    /**
     * go to registration page
     * @param event go to registration
     */
    @FXML
    private void GoToRegisterButtonAction (ActionEvent event) {
        GenericController.goToPage("../View/Registration.fxml",event);
    }
    /**
     * exits the application
     * @param event exit button pushed
     */
    @FXML
    private void Exit (ActionEvent event){
        Platform.exit();
        System.exit(0);
    }
}
