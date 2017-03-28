package application;

import Controllers.AdminController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class AdminLauncher
{
    public static void start() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AdminLauncher.class.getResource("/view/Admin.fxml"));
        AnchorPane root = loader.load();

        AdminController aCont = loader.getController();
        aCont.init();

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


}
