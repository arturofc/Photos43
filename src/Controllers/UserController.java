package Controllers;

import Models.Album;
import Models.User;
import application.AlbumLauncher;
import application.Photos;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class UserController
{
    @FXML
    public Label usernameLabel;
    @FXML
    public TableColumn<Album, String> albumNameCol;
    @FXML
    public TableColumn<Album, String> photoCountCol;
    @FXML
    public TableColumn<Album, String> dateRangeCol;
    @FXML
    public TableView<Album> albumTable = new TableView<>();

    private User owner;

    private ObservableList<Album> data;

    public void init(User u)
    {
        usernameLabel.setText(u.getName());


        data = FXCollections.observableArrayList(Album.getAlbumList(u));

        albumNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        photoCountCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().photoCount())));
        dateRangeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDateRange()));

        albumTable.setItems(data);

        owner = u;

        if (!(data.isEmpty()))
        {
            albumTable.getSelectionModel().select(0);
        }


    }


    /*
    Error Prone. Check this shit later
     */
    public void logout(ActionEvent event) throws Exception
    {
        Photos.logout(event);
    }

    public void rename(ActionEvent event) throws Exception
    {
        if (albumTable.getSelectionModel().getSelectedItem() == null)
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            invalidInput.setTitle("No Selection");
            invalidInput.setHeaderText("No Item Selected");
            invalidInput.setContentText("Make sure you have an album selected to rename");
            invalidInput.showAndWait();
            return;
        }

        Album a = albumTable.getSelectionModel().getSelectedItem();

        TextInputDialog dialog = new TextInputDialog(a.getName());
        dialog.setTitle("Rename");
        dialog.setHeaderText("Rename Album");
        dialog.setContentText("Enter a new album name");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !Album.doesAlbumNameExist(result.get(), owner))
            result.ifPresent(aName -> Album.renameAlbum(a, a.getOwner(), aName));
        else if (result.isPresent() && Album.doesAlbumNameExist(result.get(), owner))
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            invalidInput.setTitle("Duplicate");
            invalidInput.setHeaderText("Duplicate album");
            invalidInput.setContentText("Cannot rename an album that is a duplicate name");
            invalidInput.showAndWait();
            return;
        }


        data = FXCollections.observableArrayList(Album.getAlbumList(owner));
        albumNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        photoCountCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().photoCount())));
        dateRangeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDateRange()));

        albumTable.setItems(data);

    }

    public void delete(ActionEvent event)
    {
        if (data.isEmpty())
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            //invalidInput.initOwner(maingStage); FIND OUT IF THIS IS NECESSARY
            invalidInput.setTitle("Can not remove");
            invalidInput.setHeaderText("No albums");
            invalidInput.setContentText("Please make sure to have a populated album list");
            invalidInput.showAndWait();
            return;
        }
        else if (albumTable.getSelectionModel().getSelectedItem() == null)
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            invalidInput.setTitle("No Selection");
            invalidInput.setHeaderText("No Album Selected");
            invalidInput.setContentText("Make sure you have an album selected to delete");
            invalidInput.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion Confirmation");
        alert.setHeaderText("Are you sure you want to remove this album?");
        alert.setContentText("Click OK or Cancel");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK)
        {
            int remove = albumTable.getSelectionModel().getSelectedIndex();

            if (remove != data.size() - 1)
            {
                albumTable.getSelectionModel().selectNext();
            }
            Album.deleteAlbum(Album.getAlbumList(owner).get(remove));
            data.remove(remove);
            alert.close();

        }
        else
        {
            alert.close();
        }

    }

    public void open(ActionEvent event) throws IOException
    {
        if (data.isEmpty())
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            //invalidInput.initOwner(maingStage); FIND OUT IF THIS IS NECESSARY
            invalidInput.setTitle("Can not open");
            invalidInput.setHeaderText("No albums");
            invalidInput.setContentText("Please make sure to have a populated album list");
            invalidInput.showAndWait();
            return;
        }
        else if (albumTable.getSelectionModel().getSelectedItem() == null)
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            invalidInput.setTitle("No Selection");
            invalidInput.setHeaderText("No Album Selected");
            invalidInput.setContentText("Make sure you have an album selected to open");
            invalidInput.showAndWait();
            return;
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();

        AlbumLauncher.start(owner, albumTable.getSelectionModel().getSelectedItem());

    }

    public void newAlbum(ActionEvent event)
    {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename");
        dialog.setHeaderText("Rename Album");
        dialog.setContentText("Enter a new album name");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !Album.doesAlbumNameExist(result.get(), owner))
            result.ifPresent(aName -> Album.commitAlbum(new Album(aName, owner)));
        else if (result.isPresent() && Album.doesAlbumNameExist(result.get(), owner))
        {
            Alert invalidInput = new Alert(Alert.AlertType.INFORMATION);
            invalidInput.setTitle("Duplicate");
            invalidInput.setHeaderText("Duplicate New Album");
            invalidInput.setContentText("Cannot create a new album that is a duplicate name");
            invalidInput.showAndWait();
            return;
        }
        data = FXCollections.observableArrayList(Album.getAlbumList(owner));
        albumNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        photoCountCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().photoCount())));
        dateRangeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDateRange()));

        albumTable.setItems(data);
    }


}
