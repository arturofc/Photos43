package Controllers;

import Models.User;
import application.AdminLauncher;
import application.UserLauncher;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private Stage s;

    /**
     * Init the controller
     */
    public void init(Stage stage)
    {
        s = stage;

        //System.out.println("LoginLauncher Controller started");
    }

    /**
     * Returns if username and password is good
     *
     * @param username the username
     * @param password the password
     * @return true if correct false if not
     */
    private boolean login(String username, String password)
    {
        return User.checkUserAndPass(username, password);
    }

    /**
     * Submit button
     *
     * @param event handles button click
     */
    public void submit(Event event)
    {
        if (login(usernameInput.getText(), passwordInput.getText()) && User.getUser(usernameInput.getText()).isPresent())
            launchView(User.getUser(usernameInput.getText()).get(), User.isAdmin(usernameInput.getText()), event);
        else
            showPasswordError();
    }

    /**
     * Launches the album view and possibly the admin view depending on if the user is an admin
     *
     * @param u       the user passed in
     * @param isAdmin is admin or not
     * @param event   the button click event
     */
    private void launchView(User u, boolean isAdmin, Event event)
    {
        if (isAdmin)
        {
            try
            {
                UserLauncher.start(u, s);
                AdminLauncher.start(u);

            }
            catch (IOException e)
            {
                e.printStackTrace();
                showIOError();
            }

        }
        else
        {
            try
            {
                UserLauncher.start(u, s);
            }
            catch (IOException e)
            {
                e.printStackTrace();

                showIOError();
            }
        }
    }

    /**
     * Show bad combo error
     */
    private void showPasswordError()
    {
        Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
        invalidInput.setTitle("Invalid Input");
        invalidInput.setHeaderText("Invalid Input");
        invalidInput.setContentText("Username or Password Does Not Exist");
        invalidInput.showAndWait();

    }

    /**
     * Shows an io error
     */
    private void showIOError()
    {
        Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
        invalidInput.setTitle("ERROR");
        invalidInput.setHeaderText("IO Error");
        invalidInput.setContentText("IO Error launching windows");
        invalidInput.showAndWait();
    }


}
