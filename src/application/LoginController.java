package application;

import application.User;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by cal13 on 3/24/2017.
 */
public class LoginController
{
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    public void init()
    {
        User u = new User("admin","admin","admin", true);
        User.commitUser(u);
        System.out.println("LoginLauncher Controller started");

    }

    public boolean login(String username, String password)
    {
       return User.checkUserAndPass(username, password);
    }
    public void submit(Event event)
    {
        if (login(usernameInput.getText(), passwordInput.getText()) )
            launchView(User.isAdmin(usernameInput.getText()), event);
        else
            showError();
    }
    public void launchView(boolean isAdmin, Event event)
    {
            if (isAdmin)
            {
                try
                {
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                    UserLauncher.start();
                    AdminLauncher.start();

                }
                catch (IOException e)
                {
                    e.printStackTrace();

                    Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
                    invalidInput.setTitle("ERROR");
                    invalidInput.setHeaderText("IO Error");
                    invalidInput.setContentText("IO Error launching windows");
                    invalidInput.showAndWait();
                }

            }
            else
            {
                try
                {
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                    UserLauncher.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();

                    Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
                    invalidInput.setTitle("ERROR");
                    invalidInput.setHeaderText("IO Error");
                    invalidInput.setContentText("IO Error launching windows");
                    invalidInput.showAndWait();
                }
            }
    }
    public void showError()
    {
        Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
        invalidInput.setTitle("Invalid Input");
        invalidInput.setHeaderText("Invalid Input");
        invalidInput.setContentText("Username or Password Does Not Exist");
        invalidInput.showAndWait();

    }


}
