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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class AddWeightController {
    private Person User;
    @FXML private Label name;
    @FXML private TextField weight;
    @FXML private Label errorMsg;
    /**
     * set the user
     * @param User current user that is signed in
     */
    public void setUser(Person User){
        this.User = User;
    }
    /**
     * sets up the display for the user
     */
    public void setUpDisplay(){
        name.setText("Hi, " + User.getForename());
    }
    /**
     * go to the dashboard
     * @param event push the back button
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
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
        stage.show();
    }
    /**
     * validation
     * add weight
     * check if goals met
     * @param event add weight button pushed
     */
    @FXML
    private void AddWeightAction (ActionEvent event) {
        errorMsg.setText("");
        //validation
        if (weight.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            double i = Double.parseDouble(weight.getText());
            if (i>0){
                if (i>250){
                    errorMsg.setText("Error: weight greater than 250");
                    weight.setText("");
                }
            } else {
                errorMsg.setText("Error: weight negative");
                weight.setText("");
            }
        } else {
            errorMsg.setText("Error: weight not numeric");
            weight.setText("");
        }
        if(errorMsg.getText().equals("")){
            DatabaseController db = new DatabaseController();
            db.addWeight(User.getID(),weight.getText());
            boolean goalMet = db.checkGoalMet(User.getID());
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
            if (goalMet){
                controller.setGoalComplete();
            }
            controller.setUser(User);
            controller.setUpDisplay();
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreen(true);
            stage.show();
        }
    }
}
