package application;

import Controllers.SlideshowController;
import Models.Album;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class SlideshowLauncher
{
    /**
     * Launches the slideshow screen
     *
     * @param a the album to slideshow
     * @throws IOException throws an exception
     */
    public static void start(Album a) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserLauncher.class.getResource("/view/Slideshow.fxml"));
        BorderPane root = loader.load();

        SlideshowController sCont = loader.getController();
        sCont.init(a);

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Album");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }
}
