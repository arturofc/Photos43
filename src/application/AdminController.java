package application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;

import java.util.Optional;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class AdminController
{
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

        System.out.println(User.getUserList().size());

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

}
