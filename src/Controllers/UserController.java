package Controllers;

import Models.User;
import application.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class UserController
{
    @FXML
    public Label usernameLabel;

    public void init(User u)
    {
        usernameLabel.setText(u.getName());


    }


    /*
    Error Prone. Check this shit later
     */
    public void logout(ActionEvent event) throws Exception
    {
        Photos.logout(event);
    }


}
