package All;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AddFoodController {
    private Person User;
    @FXML
    private Label name;
    public void setUser(Person User){
        this.User = User;
    }
    public void setUpDisplay(){
        name.setText("Welcome " + User.getForename());
    }
}
