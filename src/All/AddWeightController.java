package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddWeightController {
    private Person User;
    @FXML private Label name;
    @FXML private TextField weight;
    public void setUser(Person User){
        this.User = User;
    }
    public void setUpDisplay(){
        name.setText("Hi, " + User.getForename());
    }
    @FXML
    private void GoToDashButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        DashboardController controller = loader.<DashboardController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void AddWeightAction (ActionEvent event) throws IOException {
        //check weight is numeric
        //check weight isnt crazy
        DatabaseController db = new DatabaseController();
        db.addWeight(User.getID(),weight.getText());
        boolean goalMet = db.checkGoalMet(User.getID());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        DashboardController controller = loader.<DashboardController>getController();
        if (goalMet){
            controller.setGoalComplete();
        }
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
}
