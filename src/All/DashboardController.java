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
import java.util.Date;

public class DashboardController {
    private Person User;
    @FXML
    private Label name;
    @FXML Label calLeft;
    public void setUser(Person User){
        this.User = User;
    }
    public void setUpDisplay(){
        name.setText("Welcome " + User.getForename());
        int totalCal= 1800;
        if (User.getGender()=='M'){
            totalCal = 2000;
        }
        DatabaseController db = new DatabaseController();
        int cb = db.getCalBurned(User.getID(),new Date());
        int cc = db.getCalConsumed(User.getID(),new Date());
       calLeft.setText(totalCal + " - " + cc + " + " + cb + " = " + (totalCal - cc + cb));
    }
    @FXML
    private void GoToAddWeightButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddWeight.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddWeightController controller = loader.<AddWeightController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void GoToAddExerciseSessionButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddExerciseSession.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddExerciseSessionController controller = loader.<AddExerciseSessionController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void GoToAddFoodButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFood.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddFoodController controller = loader.<AddFoodController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void GoToAddGoalButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddGoal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddGoalController controller = loader.<AddGoalController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
}
