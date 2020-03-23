package Controller;

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

import java.io.IOException;
import java.util.ArrayList;

public class AddFoodController {
    private Person User;
    @FXML private TextField txt_search;
    @FXML private ComboBox Foods;
    @FXML private TextField quantity;
    @FXML private ComboBox MealType;
    @FXML private Label errorMsg;
    @FXML private Label name;
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
        name.setText("Hello, " + User.getForename());
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
        GenericController.goToDash(User,event);
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
        if (quantity.getText().matches("^[1-9][0-9]*$")){
            int i = Integer.parseInt(quantity.getText());
            if (i>10){
                errorMsg.setText("Error: quantity greater than 10");
                quantity.setText("");
            }
        } else {
            errorMsg.setText("Error: quantity not positive");
            quantity.setText("");
        }
        //validation of dropdown
        if (Foods.getValue()==null) {
            errorMsg.setText("Error: food not selected");
        }else if(Foods.getValue().toString().equals("")){
            errorMsg.setText("Error: food not typed in");
        } else {
            if(!db.isStr(Foods.getValue().toString(),"foods","foodName")){
                errorMsg.setText("Error: not valid food");
                Foods.setValue("");
            }
        }
        //validation of the meal type
        if (MealType.getValue()==null) {
            errorMsg.setText("Error: meal type not selected");
        }else if(Foods.getValue().toString().equals("")){
            errorMsg.setText("Error: meal type not typed in");
        } else {
            if(!(MealType.getValue().toString().equals("Breakfast")||MealType.getValue().toString().equals("Lunch")||MealType.getValue().toString().equals("Dinner")||MealType.getValue().toString().equals("Snack"))){
                errorMsg.setText("Error: not valid meal type");
                MealType.setValue("");
            }
        }

        if (errorMsg.getText().equals("")){
            int mealId = db.addMeal(Foods.getValue().toString(),Integer.parseInt(quantity.getText()),MealType.getValue().toString());//adds meal
            db.addDiet(mealId,User.getID());//adds diet
            GenericController.goToDash(User,event);
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
