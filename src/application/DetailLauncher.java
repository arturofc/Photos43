package application;

import Controllers.DetailController;
import Models.Album;
import Models.Photo;
import Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class DetailLauncher
{
    /**
     * Launches the panel handling the photo details
     * @param u the logged in user
     * @param a the album youre in
     * @param s the photo you are opening
     * @throws IOException
     */
    public static void start(User u, Album a, Photo s) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserLauncher.class.getResource("/view/Image.fxml"));
        BorderPane root = loader.load();

        DetailController dCont = loader.getController();
        dCont.init(u, a, s);
        Stage primaryStage = new Stage();


        primaryStage.setTitle("Image Details");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }
}
