package view;

import application.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Created by cal13 on 3/24/2017.
 */
public class loginController
{
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    public void launch()
    {
        User u = new User("admin","admin","admin", true);
        User.commitUser(u);
        System.out.println("Login Controller started");

    }

    public boolean login(String username, String password)
    {
       return User.checkUserAndPass(username, password);
    }
    public void submit()
    {
        if (login(usernameInput.getText(), passwordInput.getText()) )
            launchView(User.isAdmin(usernameInput.getText()));
        else
            showError();
    }
    public void launchView(boolean isAdmin)
    {
            System.out.print(isAdmin);
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
