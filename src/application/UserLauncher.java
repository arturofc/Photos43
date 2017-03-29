package application;

import Controllers.UserController;
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
public class UserLauncher
{
    /**
     * Launches the user panel
     *
     * @param u user that is passed in when logged in.
     * @throws IOException throws an IOException
     */
    public static void start(User u, Stage s) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserLauncher.class.getResource("/view/User.fxml"));
        BorderPane root = loader.load();

        Stage primaryStage = s;

        UserController uCont = loader.getController();
        uCont.init(u, primaryStage);


        primaryStage.setTitle("Album List");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


}
