package Controller;

import Model.DatabaseController;
import All.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
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
    public void setUser(Person User){
        DatabaseController db = new DatabaseController();
        this.User = db.getAllPersonalInfo(User.getID());
    }
    public void setGoalComplete(){
        GoalDone.setText("Goal complete!");
    }
    public void setUpDisplay() {
        name.setText("Welcome " + User.getForename());
        int totalCal = 1800;
        if (User.getGender() == 'M') {
            totalCal = 2000;
        }
        DatabaseController db = new DatabaseController();
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

    @FXML
    private void GoToAddWeightButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddWeight.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddWeightController controller = loader.<AddWeightController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void GoToAddExerciseSessionButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddExerciseSession.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddExerciseSessionController controller = loader.<AddExerciseSessionController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void GoToAddFoodButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddFood.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddFoodController controller = loader.<AddFoodController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void GoToAddGoalButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/AddGoal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        AddGoalController controller = loader.<AddGoalController>getController();
        controller.setUser(User);
        controller.setUpDisplay();
        stage.show();
    }
    @FXML
    private void GoToSignOutButtonAction (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Login.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
