package Controllers;

import Models.Album;
import Models.User;
import application.Photos;
import application.UserLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;



/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class AlbumController
{
    @FXML
    public Label albumLabel;

    private User user;
    private Album album;

    public void init(User u, Album a)
    {
        user = u;
        album = a;

        albumLabel.setText(a.getName());
    }
    public void logout(ActionEvent event) throws Exception
    {
        Photos.logout(event);
    }
    public void back(ActionEvent event) throws Exception
    {
        ((Node) (event.getSource())).getScene().getWindow().hide();

        UserLauncher.start(user);
    }
}
