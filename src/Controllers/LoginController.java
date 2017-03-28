package Controllers;

import Models.User;
import application.AdminLauncher;
import application.UserLauncher;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class LoginController
{
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    public void init()
    {
        User u = new User("admin", "admin", "admin", true);
        User.commitUser(u);
        System.out.println("LoginLauncher Controller started");

    }

    private boolean login(String username, String password)
    {
        return User.checkUserAndPass(username, password);
    }

    public void submit(Event event)
    {
        if (login(usernameInput.getText(), passwordInput.getText()) && User.getUser(usernameInput.getText()).isPresent())
            launchView(User.getUser(usernameInput.getText()).get(), User.isAdmin(usernameInput.getText()), event);
        else
            showPasswordError();
    }

    private void launchView(User u, boolean isAdmin, Event event)
    {
        if (isAdmin)
        {
            try
            {
                ((Node) (event.getSource())).getScene().getWindow().hide();
                UserLauncher.start(u);
                AdminLauncher.start();

            }
            catch (IOException e)
            {
                e.printStackTrace();
                showIOError();
            }

        } else
        {
            try
            {
                ((Node) (event.getSource())).getScene().getWindow().hide();
                UserLauncher.start(u);
            }
            catch (IOException e)
            {
                e.printStackTrace();

                showIOError();
            }
        }
    }

    private void showPasswordError()
    {
        Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
        invalidInput.setTitle("Invalid Input");
        invalidInput.setHeaderText("Invalid Input");
        invalidInput.setContentText("Username or Password Does Not Exist");
        invalidInput.showAndWait();

    }

    private void showIOError()
    {
        Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
        invalidInput.setTitle("ERROR");
        invalidInput.setHeaderText("IO Error");
        invalidInput.setContentText("IO Error launching windows");
        invalidInput.showAndWait();
    }


}
