package application;

import Controllers.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class LoginLauncher
{
    public static void start() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(LoginLauncher.class.getResource("/view/Login.fxml"));
        AnchorPane root = loader.load();

        LoginController lCont = loader.getController();
        lCont.init();

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }

}
