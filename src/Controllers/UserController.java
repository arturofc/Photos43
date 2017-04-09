package Controllers;

import Models.Album;
import Models.User;
import application.AlbumLauncher;
import application.Photos;
import application.SearchLauncher;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static application.Photos.showError;

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

    private Stage s;

    public void init(User u, Stage s)
    {
        this.usernameLabel.setText(u.getName());
        this.s = s;

        this.data = FXCollections.observableArrayList(Album.getAlbumList(u));

        /*
        Handles setting the columns to their proper data.
         */
        this.albumNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        this.photoCountCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().photoCount())));
        this.dateRangeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDateRange()));

        this.albumTable.setItems(data);

        this.owner = u;

        /*
        if data is not empty set the first thing elected ie songlib
         */
        if (!(data.isEmpty()))
        {
            this.albumTable.getSelectionModel().select(0);
        }

    }
    /*
    Error Prone. Check this shit later
     */

    /**
     * Logs the user out
     *
     * @param event the button event
     * @throws Exception throws a possible exception
     */
    public void logout(ActionEvent event) throws Exception
    {
        Photos.logout(event);
    }

    /**
     * Rename the album
     *
     * @param event the button event
     * @throws Exception throws an exception
     */
    public void rename(ActionEvent event) throws Exception
    {
        /*
        If nothing is selected throw an error
         */
        if (albumTable.getSelectionModel().getSelectedItem() == null)
        {
            showError("No selection", "No item selected", "Make sure you have an album selected to rename");
            return;
        }

        /*
        Set the album to rename to aht is selcted
         */
        Album a = albumTable.getSelectionModel().getSelectedItem();

        /*
        Create a text input dialog
         */
        TextInputDialog dialog = new TextInputDialog(a.getName());
        dialog.setTitle("Rename");
        dialog.setHeaderText("Rename Album");
        dialog.setContentText("Enter a new album name");
        /*
        Wait for results
         */
        Optional<String> result = dialog.showAndWait();


        /*
        if there is a result and the trimmed result length is 0
         */
        if (result.isPresent() && result.get().trim().length() == 0)
        {
            showError("Blank name", "Blank name", "Cannot have a blank name or name with only spaces.");
            return;
        }
        /*
        if result is present and its the same as the current album
         */
        else if (result.isPresent() && result.get().trim().equals(a.getName().trim()))
        {
            showError("Same name", "Same name as selected album", "Cannot have the same name as the selected album. ");
            return;
        }
        else if (result.isPresent() && !Album.doesAlbumNameExist(result.get(), owner))
            result.ifPresent(aName -> Album.renameAlbum(a, a.getOwner(), aName));
        /*
        otherwise, if theres a result and album name does exist throw an error
         */

        else if (result.isPresent() && Album.doesAlbumNameExist(result.get(), owner))
        {
            showError("Duplicate", "Duplicate album", "Cannot rename an album that is a duplicate name");
            return;
        }

        /*
        Redo entire table
         */
        data = FXCollections.observableArrayList(Album.getAlbumList(owner));
        albumNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        photoCountCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().photoCount())));
        dateRangeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDateRange()));

        albumTable.setItems(data);

    }

    /**
     * Delete the selected album
     *
     * @param event the button event
     */
    public void delete(ActionEvent event)
    {
        if (data.isEmpty())
        {
            showError("Can not remove", "No albums", "Please make sure to have a populated album list");
            return;
        }
        else if (albumTable.getSelectionModel().getSelectedItem() == null)
        {
            showError("No selection", "No Album Selected", "Make sure you have an album selected to delete");
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
    
    /**
     * Search Photos by Tag
     * @param event the button event
     * @throws IOException 
     */
    public void search(ActionEvent event) throws IOException {
    	
    	// start the Search interface
    	SearchLauncher.start(owner, s);
    	
    }
    
    /**
     * Open the album
     *
     * @param event the button event
     * @throws IOException throws an exception
     */
    public void open(ActionEvent event) throws IOException
    {
        if (data.isEmpty())
        {
            showError("Can not open", "No albums", "Please make sure to have a populated album list");
            return;
        }
        else if (albumTable.getSelectionModel().getSelectedItem() == null)
        {
            showError("No Selection", "No Album Selected", "Make sure you have an album selected to open");
            return;
        }

        AlbumLauncher.start(owner, albumTable.getSelectionModel().getSelectedItem(), s);

    }

    /**
     * Create a new album
     *
     * @param event the button event
     */
    public void newAlbum(ActionEvent event)
    {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename");
        dialog.setHeaderText("New Album");
        dialog.setContentText("Enter a new album name");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && result.get().trim().length() == 0)
        {
            showError("Blank name", "Blank name", "Cannot have a blank name or name with only spaces.");
            return;
        }
        else if (result.isPresent() && !Album.doesAlbumNameExist(result.get(), owner))
            result.ifPresent(aName -> Album.commitAlbum(new Album(aName, owner)));

        else if (result.isPresent() && Album.doesAlbumNameExist(result.get(), owner))
        {
            showError("Duplicate", "Duplicate New Album", "Cannot create a new album that is a duplicate name");
            return;
        }

        data = FXCollections.observableArrayList(Album.getAlbumList(owner));

        albumNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        photoCountCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Integer.toString(cellData.getValue().photoCount())));
        dateRangeCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDateRange()));

        albumTable.setItems(data);
    }

}
