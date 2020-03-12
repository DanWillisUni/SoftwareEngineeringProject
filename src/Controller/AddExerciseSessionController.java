package Controller;
//javafx imports
import Model.DatabaseController;
import Model.Person;
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
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
//java imports
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class AddExerciseSessionController {
    private Person User; //person who is currently logged in
    @FXML private TextField txt_search;//search box
    @FXML private ComboBox Exercise;//exercises drop down
    @FXML private TextField duration;//duration text box
    @FXML private TextField calBurned;//calories burned text box
    @FXML private Label errorMsg;//error message label
    @FXML private Label name;
    /**
     * Sets the current user that is signed into the health tracker
     * @param User Person that is signed in
     */
    public void setUser(Person User){
        this.User = User;
    }
    /**
     * Sets up the drop down for all the exercises
     */
    public void setUpDisplay(){
        try {
            DatabaseController db = new DatabaseController();
            ArrayList<String> results = db.getAllLike("","exercise","exerciseName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Exercise.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText("Hello, " + User.getForename());
    }
    /**
     * Loads the dashboard on the push of the back button
     * @param event button push
     */
    @FXML
    private void GoToDashButtonAction (ActionEvent event) {
        GenericController.goToDash(User,event);
    }
    /**
     * Adds exercise when button pushed
     * @param event button push
     */
    @FXML
    private void AddExerciseSessionAction (ActionEvent event) {
        errorMsg.setText("");
        DatabaseController db = new DatabaseController();
        boolean validCal = false;
        Boolean validSport = false;
        //validation of calories burned
        if (!calBurned.getText().equals("")){
            if (calBurned.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
                int i = Integer.parseInt(calBurned.getText());
                if (i>0){
                    if (i>2500){
                        errorMsg.setText("Error: calories > 2500");
                        calBurned.setText("");
                    } else {
                        validCal = true;
                    }
                } else {
                    errorMsg.setText("Error: calories negative");
                    calBurned.setText("");
                }
            } else {
                errorMsg.setText("Error: calories not numeric");
                calBurned.setText("");
            }
        } else { // if no calories burned entered then validate the exercise type
            if (Exercise.getValue()==null) {
                errorMsg.setText("Error: sport not selected");
            }else if(Exercise.getValue().toString().equals("")){
                errorMsg.setText("Error: not typed in");
            } else {
                if(!db.isStr(Exercise.getValue().toString(),"exercise","exerciseName")){
                    errorMsg.setText("Error: not valid sport");
                    Exercise.setValue("");
                } else {
                    validSport = true;
                }
            }
        }
        //validate the duration
        if (duration.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            int i = Integer.parseInt(duration.getText());
            if (i>0){
                if (i>240){
                    errorMsg.setText("Error: duration greater than 4 hours");
                    duration.setText("");
                }
            } else {
                errorMsg.setText("Error: duration negative");
                duration.setText("");
            }
        } else {
            errorMsg.setText("Error: duration not numeric");
            duration.setText("");
        }
        //if no errors in validation
        if (errorMsg.getText()==""){
            int caloriesBurned = 0;
            int sportID = 0;
            BigDecimal durationDec = new BigDecimal(duration.getText());//set the duration
            //set the sport if it is valid
            if (validSport){
                sportID = db.getIDFromName(Exercise.getValue().toString(),"exercise","exerciseName","idExerciseType");
                //if the calories are not valid then calculate it
                if (!validCal){
                    caloriesBurned = durationDec.multiply(new BigDecimal(db.getCalsBurnedFromID(sportID))).intValue();
                }
            }
            //set the calories if they are valid
            if (validCal){
                caloriesBurned = Integer.parseInt(calBurned.getText());
            }
            db.addExerciseLink(db.addExerciseSession(durationDec,sportID,caloriesBurned),User.getID());//adds the exercise link to the database
            GenericController.goToDash(User,event);//go to the dashboard
        }
    }
    /**
     * get all like search box and put it in the drop down
     * @param event button pushed
     */
    @FXML
    private void goSearch(ActionEvent event) {
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
