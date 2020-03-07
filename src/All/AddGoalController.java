package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class AddGoalController {
    private Person User;
    @FXML private TextField TargetWeight;
    @FXML private DatePicker targetDate;
    @FXML private Label errorMsg;
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
        errorMsg.setText("");
        if (TargetWeight.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            int i = Integer.parseInt(TargetWeight.getText());
            if (i>0){
                if (i>250){
                    errorMsg.setText("Error: target greater than 250");
                }
            } else {
                errorMsg.setText("Error: target negative");
            }
        } else {
            errorMsg.setText("Error: target not numeric");
        }
        if(targetDate.getValue()!=null){
            Long d = Date.from(Instant.from(targetDate.getValue().atStartOfDay(ZoneId.systemDefault()))).getTime();
            Long c = new Date().getTime();
            if (c<=d){
                if (c + 31536000000L < d){
                    errorMsg.setText("Error: target to far");
                }
            } else {
                errorMsg.setText("Error: target date in the past");
            }
        }else {
            errorMsg.setText("Error: Date not selected");
        }
        if (errorMsg.getText().equals("")){
            DatabaseController db = new DatabaseController();
            db.addGoal(User.getID(),Integer.parseInt(TargetWeight.getText()), Date.from(Instant.from(targetDate.getValue().atStartOfDay(ZoneId.systemDefault()))));
            GoToDashButtonAction(event);
        }
    }
}
