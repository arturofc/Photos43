package application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class AdminController
{
    @FXML
    public TableColumn<User, String> passwordCol;
    @FXML
    public TableColumn<User, String> usernameCol;
    @FXML
    private TableView<User> userTable = new TableView<>();

    public void init()
    {
        final ObservableList<User> data = FXCollections.observableArrayList(User.getUserList());

        usernameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUsername()));
        passwordCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPassword()));

        userTable.setItems(data);

        System.out.println(User.getUserList().size());



    }
}
