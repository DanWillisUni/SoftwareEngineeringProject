package All;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class RegistrationController {
    @FXML private Text actiontarget;
    @FXML
    protected void RegisterHandleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Register button pressed");
        //do registration stuff
    }
}
