package All;

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
    public void setUser(Person User){
        this.User = User;
    }
    public void setUpDisplay(){
        try {
            DatabaseController db = new DatabaseController();
            ArrayList<String> results = db.getAllExercisesLike("");
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
        //checks if duration is craxy
        //checks if calcount is crazy
        //checks if anything is entered
        int sportID = 0;
        BigDecimal durationDec = new BigDecimal(duration.getText());
        int caloriesBurned = 0;
        if (!calBurned.getText().equals("")){
            caloriesBurned = Integer.parseInt(calBurned.getText());
        }
        DatabaseController db = new DatabaseController();
        if (Exercise.getValue()!=null&&!Exercise.getValue().toString().equals("other")){
            sportID = db.getIDFromName(Exercise.getValue().toString());
            if (calBurned.getText().equals("")){
                caloriesBurned = durationDec.multiply(new BigDecimal(db.getCalsBurnedFromID(sportID))).intValue();
            }
        }
        db.addExerciseSession(durationDec,sportID,caloriesBurned,User.getID());
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
    private void goSearch(ActionEvent event) throws IOException {
        try {
            String toSearch = txt_search.getText();
            DatabaseController db = new DatabaseController();
            ArrayList<String> results = db.getAllExercisesLike(toSearch);
            ObservableList<String> observableList = FXCollections.observableList(results);
            Exercise.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
