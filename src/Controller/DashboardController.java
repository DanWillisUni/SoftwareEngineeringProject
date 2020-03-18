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
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
    @FXML Label BMI;
    /**
     * sets the user to the user that is logged in
     * @param User Person object logged in
     */
    public void setUser(Person User){
        DatabaseController db = new DatabaseController();
        this.User = db.getAllPersonalInfo(User.getID());
    }
    /**
     * Sets up the display
     * gets any goals that have expired or been competed and removes them with a message
     * gets bmi
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
        if(db.removeOverdueGoals(User.getID())){
            GoalDone.setText("Goal Removed as it was overdue");
        }
        if (db.checkGoalMet(User.getID())){
            GoalDone.setText("Goal complete!");
        }
        int goalWeight = db.getClosestGoal(User.getID());
        if (goalWeight != -1){
            nextGoal.setText("Up coming goal: " + goalWeight);
        }
        int cb = db.getCalBurned(User.getID(), new Date());
        int cc = db.getCalConsumed(User.getID(), new Date());
        calLeft.setText(totalCal + " - " + cc + " + " + cb + " = " + (totalCal - cc + cb));

        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        double bmi = db.getCurrentWeight(User.getID())/Math.pow((User.getHeight().doubleValue()/100.0),2.0);
        BMI.setText("Your BMI is: " + df.format(bmi));

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddWeight.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddWeightController controller = loader.<AddWeightController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * goes to the add exercise page
     * @param event add exercise button pressed
     */
    @FXML
    private void GoToAddExerciseSessionButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddExerciseSession.fxml"));
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
    /**
     * goes to the add food page
     * @param event add food button pushed
     */
    @FXML
    private void GoToAddFoodButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddFood.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddFoodController controller = loader.<AddFoodController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * goes to the add goal page
     * @param event add goal button pressed
     */
    @FXML
    private void GoToAddGoalButtonAction (ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddGoal.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddGoalController controller = loader.<AddGoalController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }
    /**
     * signs out the user by not passing the user and going to login page
     * @param event sign out button pushed
     */
    @FXML
    private void GoToSignOutButtonAction (ActionEvent event) {
        GenericController.goToPage("../View/Login.fxml",event);
    }
}
