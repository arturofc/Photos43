package view;

import application.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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
        User u = new User("admin","admin","admin");
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
            launchView();
        else
            showError();
    }
    public void launchView()
    {

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
