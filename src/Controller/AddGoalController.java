package Controller;
//my imports
import Model.DatabaseController;
import Model.Person;
//fx imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
//java imports
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class AddGoalController {
    private Person User;
    @FXML private TextField TargetWeight;
    @FXML private DatePicker targetDate;
    @FXML private Label errorMsg;
    /**
     * sets the user that is signed in
     * @param User person to set to
     */
    public void setUser(Person User){
        this.User = User;
    }
    /**
     * sets up the display
     */
    public void setUpDisplay(){

    }
    /**
     * go to the dashboard
     * @param event back button pressed
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event){
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
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * adds goal via db.addgoal
     * @param event add goal button pressed
     */
    @FXML
    private void AddWeightGoalButtonAction (ActionEvent event) {
        errorMsg.setText("");
        //validate target weight
        if (TargetWeight.getText().matches("^[1-9][0-9]*$")){
            int i = Integer.parseInt(TargetWeight.getText());
            if (i>0){
                if (i>250){
                    errorMsg.setText("Error: target greater than 250");
                    TargetWeight.setText("");
                }
            } else {
                errorMsg.setText("Error: target negative");
                TargetWeight.setText("");
            }
        } else {
            errorMsg.setText("Error: target not numeric");
            TargetWeight.setText("");
        }
        //validate target date
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
