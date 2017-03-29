package application;

import Controllers.AlbumController;
import Controllers.UserController;
import Models.Album;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by cal13 on 3/28/2017.
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
    public static void start(User u, Album a) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserLauncher.class.getResource("/view/Album.fxml"));
        AnchorPane root = loader.load();

        AlbumController aCont = loader.getController();
        aCont.init(u, a);
        Stage primaryStage = new Stage();

        primaryStage.setTitle("Album");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }


}
