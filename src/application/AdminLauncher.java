package application;

import Controllers.AdminController;
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
public class AdminLauncher
{
    /**
     * Start Admin Panel
     *
     * @throws IOException throws an IO Exception.
     */
    public static void start(User u) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AdminLauncher.class.getResource("/view/Admin.fxml"));
        BorderPane root = loader.load();

        AdminController aCont = loader.getController();
        aCont.init(u);

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


}
