package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.loginController;

import java.io.IOException;

/**
 * Created by cal13 on 3/27/2017.
 */
public class Login extends Application
{
    public void start(Stage primaryStage) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/Login.fxml"));
        AnchorPane root = loader.load();

        loginController lCont = loader.getController();
        lCont.launch();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    public static void launchLogin()
    {
        launch();
    }
}
