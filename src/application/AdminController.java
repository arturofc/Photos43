package application;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class AdminController
{
    @FXML
    public TextField name;
    @FXML
    public TextField username;
    @FXML
    public TextField password;

    @FXML
    public CheckBox isAdminBox;

    private ObservableList<User> data;
    @FXML
    public TableColumn<User, String> passwordCol;
    @FXML
    public TableColumn<User, String> usernameCol;
    @FXML
    private TableView<User> userTable = new TableView<>();



    public void init()
    {
        data = FXCollections.observableArrayList(User.getUserList());

        usernameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        passwordCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPassword()));

        userTable.setItems(data);

    }

    public void deleteUser(ActionEvent actionEvent)
    {
        if (data.isEmpty())
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            //invalidInput.initOwner(maingStage); FIND OUT IF THIS IS NECESSARY
            invalidInput.setTitle("Can not remove");
            invalidInput.setHeaderText("No users");
            invalidInput.setContentText("Please make sure to have a populated user list");
            invalidInput.showAndWait();
            return;
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you sure you want to remove this user?");
        alert.setContentText("Click OK or Cancel");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK)
        {
            int remove = userTable.getSelectionModel().getSelectedIndex();
            if (remove != data.size() - 1)
            {
                userTable.getSelectionModel().selectNext();
            }
            User.deleteUser(User.getUserList().get(remove));
            data.remove(remove);
            alert.close();

        }
        else
        {
            alert.close();
        }
    }

    public void addUser(ActionEvent event) throws IOException
    {
        ((Node) (event.getSource())).getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AdminLauncher.class.getResource("/view/AddUser.fxml"));
        AnchorPane root = loader.load();

        Stage primaryStage = new Stage();

        primaryStage.setTitle("Add New User");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public void closeAddUser(ActionEvent event) throws IOException
    {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        AdminLauncher.start();
    }
    public void addUserButton(ActionEvent event) throws IOException
    {
        if (username.getText().isEmpty())
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            invalidInput.setTitle("Error");
            invalidInput.setHeaderText("Username Required");
            invalidInput.setContentText("A username is required");
            invalidInput.showAndWait();
            return;

        }
        else if (User.doesUsernameExist(username.getText()))
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            invalidInput.setTitle("Error");
            invalidInput.setHeaderText("Username exists");
            invalidInput.setContentText("Cannot add a username that exists");
            invalidInput.showAndWait();
            return;
        }
        else if (name.getText().isEmpty())
        {
            if (isAdminBox.isSelected())
                User.commitUser(new User(username.getText(), username.getText(), password.getText(), true));
            else
                User.commitUser(new User(username.getText(), username.getText(), password.getText()));
        }
        else
        {
            if (isAdminBox.isSelected())
                User.commitUser(new User(name.getText(), username.getText(), password.getText(), true));
            else
                User.commitUser(new User(name.getText(), username.getText(), password.getText()));
        }

        ((Node) (event.getSource())).getScene().getWindow().hide();

        AdminLauncher.start();

    }

}
