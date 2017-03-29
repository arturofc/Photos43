package application;


import Controllers.LoginController;
import com.sun.javafx.stage.StageHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Photos extends Application
{
    /**
     * Starts Login (For when program first runs)
     *
     * @param primaryStage the stage that is passed in by launch
     * @throws IOException throws an IOException
     */
    public void start(Stage primaryStage) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/Login.fxml"));
        AnchorPane root = loader.load();

        LoginController lCont = loader.getController();
        lCont.init();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    /**
     * Global logout function
     *
     * @param event the button event that is passed in
     * @throws Exception throws an Exception
     */
    public static void logout(ActionEvent event) throws Exception
    {

        while (StageHelper.getStages().size() != 0)
        {
            StageHelper.getStages().get(0).hide();
        }

        ((Node) (event.getSource())).getScene().getWindow().hide();

        LoginLauncher.start();
    }

    /**
     * Global platform exit
     */
    public static void exit()
    {
        Platform.exit();
    }

    /**
     * Main
     *
     * @param args Argggggghhhs
     */
    public static void main(String[] args)
    {
        launch(args);
    }


}
