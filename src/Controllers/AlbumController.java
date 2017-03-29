package Controllers;

import Models.Album;
import Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by cal13 on 3/28/2017.
 */
public class AlbumController
{
    @FXML
    public Label albumLabel;

    public void init(User u, Album a)
    {
        albumLabel.setText(a.getName());
    }
}
