package application;

import Controllers.AlbumController;
import Controllers.UserController;
import Models.Album;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class AlbumLauncher
{
    /**
     * Launches the album panel
     *
     * @param u user that is passed in when logged in.
     * @param a the album being launched
     * @throws IOException throws an IOException
     */
    public static void start(User u, Album a, Stage s) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserLauncher.class.getResource("/view/Album.fxml"));
        BorderPane root = loader.load();

        AlbumController aCont = loader.getController();
        aCont.init(u, a, s);

        s.setTitle("Album");
        s.setScene(new Scene(root));
        s.setResizable(false);
        s.show();


    }


}
