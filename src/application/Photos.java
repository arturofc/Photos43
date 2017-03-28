package application;


import Controllers.LoginController;
import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.stage.StageHelper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Photos extends Application
{
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

    public static void logout(ActionEvent event) throws Exception
    {
        
        while (StageHelper.getStages().size() != 0)
        {
            StageHelper.getStages().get(0).hide();
        }

        ((Node) (event.getSource())).getScene().getWindow().hide();

        LoginLauncher.start();
    }
    public static void main(String[] args)
    {
        launch(args);
    }




}
