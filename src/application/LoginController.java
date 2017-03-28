package application;

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
        if (login(usernameInput.getText(), passwordInput.getText()))
            launchView(User.isAdmin(usernameInput.getText()), event);
        else
            showPasswordError();
    }

    private void launchView(boolean isAdmin, Event event)
    {
        if (isAdmin)
        {
            try
            {
                ((Node) (event.getSource())).getScene().getWindow().hide();
                UserLauncher.start();
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
                UserLauncher.start();
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
