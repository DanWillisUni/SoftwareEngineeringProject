package Controller;

import Model.DatabaseController;
import All.Person;
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
import java.util.ArrayList;

public class AddFoodController {
    private Person User;
    @FXML private TextField txt_search;
    @FXML private ComboBox Foods;
    @FXML private TextField quantity;
    @FXML private ComboBox MealType;
    @FXML private Label errorMsg;
    /**
     * sets the user to the user signed in
     * @param User logged in user
     */
    public void setUser(Person User){
        this.User = User;
    }
    /**
     * sets the drop down of food to all the food
     */
    public void setUpDisplay(){
        try {
            DatabaseController db = new DatabaseController();
            ArrayList<String> results = db.getAllLike("","foods","foodName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Foods.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * go to the dashboard
     * @param event button pushed
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
//        DashboardController controller = loader.<DashboardController>getController();
//        controller.setUser(User);
//        controller.setUpDisplay();
        stage.show();
    }
    /**
     * adds meal
     * adds diet
     * goto dashboard
     * @param event add food button pressed
     */
    @FXML
    private void AddFoodsAction (ActionEvent event){
        errorMsg.setText("");
        DatabaseController db = new DatabaseController();
        //validation for quantity
        if (quantity.getText().matches("^([1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$")){
            int i = Integer.parseInt(quantity.getText());
            if (i>0){
                if (i>10){
                    errorMsg.setText("Error: quantity greater than 10");
                }
            } else {
                errorMsg.setText("Error: quantity negative");
            }
        } else {
            errorMsg.setText("Error: quantity not numeric");
        }
        //validation of dropdown
        if (Foods.getValue()==null) {
            errorMsg.setText("Error: food not selected");
        }else if(Foods.getValue().toString().equals("")){
            errorMsg.setText("Error: not typed in");
        } else {
            if(!db.isStr(Foods.getValue().toString(),"foods","foodName")){
                errorMsg.setText("Error: not valid food");
            }
        }
        //validation of the meal type
        if (MealType.getValue()==null) {
            errorMsg.setText("Error: meal type not selected");
        }else if(Foods.getValue().toString().equals("")){
            errorMsg.setText("Error: meal type not typed in");
        } else {
            if(!Foods.getValue().toString().equals("Breakfast")&&!Foods.getValue().toString().equals("Lunch")&&!Foods.getValue().toString().equals("Dinner")&&!Foods.getValue().toString().equals("Snack")){
                errorMsg.setText("Error: not valid meal type");
            }
        }

        if (errorMsg.getText().equals("")){
            int mealId = db.addMeal(Foods.getValue().toString(),Integer.parseInt(quantity.getText()),MealType.getValue().toString());//adds meal
            db.addDiet(mealId,User.getID());//adds diet
            GoToDashButtonAction(event);
        }
    }
    /**
     * adjusts the result in the drop down of food
     * @param event search button pushed
     */
    @FXML
    private void goSearch(ActionEvent event) {
        try {
            String toSearch = txt_search.getText();
            DatabaseController db = new DatabaseController();
            ArrayList<String> results = db.getAllLike(toSearch,"foods","foodName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Foods.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
