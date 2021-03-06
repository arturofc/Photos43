package application;

import Controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class LoginLauncher
{
    /**
     * Starts the login screen
     *
     * @throws IOException throws an IOException
     */
    public static void start() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(LoginLauncher.class.getResource("/view/Login.fxml"));
        VBox root = loader.load();

        Stage primaryStage = new Stage();

        LoginController lCont = loader.getController();
        lCont.init(primaryStage);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }

}
