package All;

import com.mysql.jdbc.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AddExerciseSessionController {
    private Person User;
    @FXML private TextField txt_search;
    @FXML private ComboBox Exercise;
    @FXML private TextField duration;
    @FXML private TextField calBurned;
    @FXML private Label errorMsg;
    public void setUser(Person User){
        this.User = User;
    }
    public void setUpDisplay(){
        try {
            DatabaseController db = new DatabaseController();
            ArrayList<String> results = db.getAllLike("","exercise","exerciseName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Exercise.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    private void AddExerciseSessionAction (ActionEvent event) throws IOException {
        errorMsg.setText("");
        DatabaseController db = new DatabaseController();
        boolean validCal = false;
        Boolean validSport = false;
        if (!calBurned.getText().equals("")){
            if (calBurned.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
                int i = Integer.parseInt(calBurned.getText());
                if (i>0){
                    if (i>2500){
                        errorMsg.setText("Error: calories > 2500");
                    } else {
                        validCal = true;
                    }
                } else {
                    errorMsg.setText("Error: calories negative");
                }
            } else {
                errorMsg.setText("Error: calories not numeric");
            }
        } else {
            if (Exercise.getValue()==null) {
                errorMsg.setText("Error: sport not selected");
            }else if(Exercise.getValue().toString().equals("")){
                errorMsg.setText("Error: not typed in");
            } else {
                if(!db.isExercise(Exercise.getValue().toString())){
                    errorMsg.setText("Error: not valid sport");
                } else {
                    validSport = true;
                }
            }
        }

        if (duration.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            int i = Integer.parseInt(duration.getText());
            if (i>0){
                if (i>240){
                    errorMsg.setText("Error: duration greater than 4 hours");
                }
            } else {
                errorMsg.setText("Error: duration negative");
            }
        } else {
            errorMsg.setText("Error: duration not numeric");
        }

        if (errorMsg.getText()==""){
            int caloriesBurned = 0;
            int sportID = 0;
            BigDecimal durationDec = new BigDecimal(duration.getText());
            if (validSport){
                sportID = db.getIDFromName(Exercise.getValue().toString());
                if (!validCal){
                    caloriesBurned = durationDec.multiply(new BigDecimal(db.getCalsBurnedFromID(sportID))).intValue();
                }
            }
            if (validCal){
                caloriesBurned = Integer.parseInt(calBurned.getText());
            }
            db.addExerciseLink(db.addExerciseSession(durationDec,sportID,caloriesBurned),User.getID());
            GoToDashButtonAction(event);
        }
    }
    @FXML
    private void goSearch(ActionEvent event) throws IOException {
        try {
            String toSearch = txt_search.getText();
            DatabaseController db = new DatabaseController();
            ArrayList<String> results = db.getAllLike(toSearch,"exercise","exerciseName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Exercise.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
