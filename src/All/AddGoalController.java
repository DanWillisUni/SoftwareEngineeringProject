package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddGoalController {
    private Person User;
    @FXML private TextField TargetWeight;
    @FXML private DatePicker targetDate;
    public void setUser(Person User){
        this.User = User;
    }
    public void setUpDisplay(){

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
    private void AddWeightGoalButtonAction (ActionEvent event) throws IOException {

        GoToDashButtonAction(event);
    }
}
