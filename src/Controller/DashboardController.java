package Controller;

import Model.DatabaseController;
import Model.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardController {
    private Person User;
    @FXML
    private Label name;
    @FXML Label calLeft;
    @FXML Label GoalDone;
    @FXML LineChart WeightTracking;
    @FXML Label nextGoal;
    /**
     * sets the user to the user that is logged in
     * @param User Person object logged in
     */
    public void setUser(Person User){
        DatabaseController db = new DatabaseController();
        this.User = db.getAllPersonalInfo(User.getID());
    }
    /**
     * Sets the text to say that a goal was completed
     */
    public void setGoalComplete(){
        GoalDone.setText("Goal complete!");
    }
    /**
     * sets the label to say that a goal was removed
     */
    public void setGoalRemove(){
        GoalDone.setText("Goal Removed as it was overdue");
    }
    /**
     * Sets up the display
     * gets the calories of that day both consumed and burned and works out calories left
     * gets the weight of the upcoming goal
     * gets the weights and dates
     * only displays the chart of the last 2 weeks to track weight
     */
    public void setUpDisplay() {
        name.setText("Welcome " + User.getForename());
        int totalCal = 1800;
        if (User.getGender() == 'M') {
            totalCal = 2000;
        }
        DatabaseController db = new DatabaseController();
        int goalWeight = db.getClosestGoal(User.getID());
        if (goalWeight != -1){
            nextGoal.setText("Up coming goal: " + goalWeight);
        }

        int cb = db.getCalBurned(User.getID(), new Date());
        int cc = db.getCalConsumed(User.getID(), new Date());
        calLeft.setText(totalCal + " - " + cc + " + " + cb + " = " + (totalCal - cc + cb));

        ArrayList<Integer> weights = db.getWeightTrackingWeight(User.getID());
        ArrayList<java.util.Date> dates = db.getWeightTrackingDate(User.getID());
        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < weights.size(); i++) {
            series.getData().add(new XYChart.Data<Number,Number>(dates.get(i).getTime(), weights.get(i)));
        }
        series.setName("Weight");
        WeightTracking.getData().add(series);

        NumberAxis xAxis = (NumberAxis) WeightTracking.getXAxis();
        xAxis.setUpperBound(new Date().getTime() + 86400000L);
        xAxis.setLowerBound(new Date().getTime() - 1296000000L);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number n) {
                long i = n.longValue();
                java.util.Date date = new Date(i);
                DateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                return  newFormat.format(date);
            }
            @Override
            public Number fromString(String string) {
                return 0;
            }
        });
    }
    /**
     * take the user to the add weight button
     * @param event button pushed to add weight
     */
    @FXML
    private void GoToAddWeightButtonAction (ActionEvent event) {
        gotoPage("../View/AddWeight.fxml",User,event);
    }
    /**
     * goes to the add exercise page
     * @param event add exercise button pressed
     */
    @FXML
    private void GoToAddExerciseSessionButtonAction (ActionEvent event) {
        gotoPage("../View/AddExerciseSession.fxml",User,event);
    }
    /**
     * goes to the add food page
     * @param event add food button pushed
     */
    @FXML
    private void GoToAddFoodButtonAction (ActionEvent event) {
        gotoPage("../View/AddFood.fxml",User,event);
    }
    /**
     * goes to the add goal page
     * @param event add goal button pressed
     */
    @FXML
    private void GoToAddGoalButtonAction (ActionEvent event) {
        gotoPage("../View/AddGoal.fxml",User,event);
    }
    /**
     * signs out the user by not passing the user and going to login page
     * @param event sign out button pushed
     */
    @FXML
    private void GoToSignOutButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Login.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * Change page displayed to the user
     * @param page file to go to
     * @param User current user signed in
     * @param event button pressed
     */
    private void gotoPage(String page,Person User,ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(page));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddExerciseSessionController controller = loader.<AddExerciseSessionController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
}
