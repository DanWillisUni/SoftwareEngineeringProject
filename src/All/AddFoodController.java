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
import java.util.ArrayList;

public class AddFoodController {
    private Person User;
    @FXML private TextField txt_search;
    @FXML private ComboBox Foods;
    @FXML private TextField quantity;
    @FXML private ComboBox MealType;
    public void setUser(Person User){
        this.User = User;
    }
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
    private void AddFoodsAction (ActionEvent event) throws IOException {
        //checks if anything is entered
        DatabaseController db = new DatabaseController();
        int mealId = db.addMeal(Foods.getValue().toString(),Integer.parseInt(quantity.getText()),MealType.getValue().toString());
        db.addDiet(mealId,User.getID());
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
            ArrayList<String> results = db.getAllLike(toSearch,"foods","foodName");
            ObservableList<String> observableList = FXCollections.observableList(results);
            Foods.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
