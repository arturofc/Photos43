package application;

import Controllers.LoginController;
import Models.Album;
import Models.Photo;
import Models.User;
import com.sun.javafx.stage.StageHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

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

        Font.loadFont(getClass().getResourceAsStream("/src/fonts/Roboto-Black.ttf"), 14);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/Login.fxml"));
        VBox root = loader.load();


        LoginController lCont = loader.getController();
        lCont.init(primaryStage);

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
     * A global show error function making it easier to create errors
     * @param t
     * @param h
     * @param c
     */
    public static void showError(String t, String h, String c)
    {
        Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
        invalidInput.setTitle(t);
        invalidInput.setHeaderText(h);
        invalidInput.setContentText(c);
        invalidInput.showAndWait();
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
        /*
        User u = User.getUser("stock").get();


        File f1 = new File("stockPhotos/image1.jpg");
        File f2 = new File("stockPhotos/image2.png");
        File f3 = new File("stockPhotos/image3.jpg");
        File f4 = new File("stockPhotos/image4.jpg");
        File f5 = new File("stockPhotos/image5.jpg");
        File f6 = new File("stockPhotos/image6.jpg");
        File f7 = new File("stockPhotos/image7.jpg");

        Photo p1 = new Photo("image1", f1);
        Photo p2 = new Photo("image2", f2);
        Photo p3 = new Photo("image3", f3);
        Photo p4 = new Photo("image4", f4);
        Photo p5 = new Photo("image5", f5);
        Photo p6 = new Photo("image6", f6);
        Photo p7 = new Photo("image7", f7);

        HashSet<Photo> pSet = new HashSet<>();

        pSet.add(p1);
        pSet.add(p2);
        pSet.add(p3);
        pSet.add(p4);
        pSet.add(p5);
        pSet.add(p6);
        pSet.add(p7);

        Album a = new Album("stock", u);
        a.setPhotos(pSet);
        Album.commitAlbum(a);
        */


        launch(args);
    }


}
