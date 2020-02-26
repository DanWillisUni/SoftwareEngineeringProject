package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    private Person User;
    @FXML
    private Label name;
    public void setUser(Person User){
        this.User = User;
    }
    public void setUpDisplay(){
        name.setText("Welcome " + User.getForename());
    }
    @FXML
    private void GoToAddWeightButtonAction (ActionEvent event) throws IOException {
        Parent RegistrationParent = FXMLLoader.load(getClass().getResource("AddWeight.fxml"));
        Scene scene = new Scene(RegistrationParent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void GoToAddExerciseSessionButtonAction (ActionEvent event) throws IOException {
        Parent RegistrationParent = FXMLLoader.load(getClass().getResource("AddExerciseSession.fxml"));
        Scene scene = new Scene(RegistrationParent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void GoToAddFoodButtonAction (ActionEvent event) throws IOException {
        //next 5 lines changes the page
        Parent RegistrationParent = FXMLLoader.load(getClass().getResource("AddFood.fxml"));
        Scene scene = new Scene(RegistrationParent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void GoToAddFriendButtonAction (ActionEvent event) throws IOException {
        //next 5 lines changes the page
        Parent RegistrationParent = FXMLLoader.load(getClass().getResource("AddFriend.fxml"));
        Scene scene = new Scene(RegistrationParent);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
