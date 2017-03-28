package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by cal13 on 3/27/2017.
 */
public class UserLauncher
{
    public static void start() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserLauncher.class.getResource("/view/User.fxml"));
        AnchorPane root = loader.load();


        Stage primaryStage = new Stage();

        primaryStage.setTitle("Album List");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();


    }



}
