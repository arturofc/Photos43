package Controllers;

import Models.Album;
import Models.Photo;
import Models.User;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class DetailController
{
    @FXML
    public ImageView detailImagePane;
    @FXML
    public Label captionLabel;
    @FXML
    public Label filenameLabel;
    @FXML
    public Label dateLabel;
    @FXML
    public TextField keyInput;
    @FXML
    public Button addButton;
    @FXML
    public TextField valInput;
    @FXML
    public Button editDate;
    @FXML
    public Button editCaption;
    @FXML
    public TableView<Pair<String, String>> keyValTable;
    @FXML
    public TableColumn<Pair<String, String>, String> keyCol;
    @FXML
    public TableColumn<Pair<String, String>, String> valCol;


    private User user;
    private Album album;
    private Photo photo;
    private ObservableList<Pair<String, String>> data;

    public void init(User u, Album a, Photo p)
    {
            try
            {
                Image i = new Image(new FileInputStream(p.getPhotoFile()));

                detailImagePane.setImage(i);
                captionLabel.setText(p.getName());
                filenameLabel.setText(p.getPhotoFile().getName());
                dateLabel.setText(p.getDate().toString());

                user = u;
                album = a;
                photo = p;

                ArrayList<Pair<String,String>> tempList = new ArrayList<>();

                for (Map.Entry<String, HashSet<String>> e : photo.getTags().entrySet())
                {
                    e.getValue().forEach(x -> tempList.add(new Pair<>(e.getKey(), x)));
                }

                data = FXCollections.observableArrayList(tempList);

                keyCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
                valCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue()));
                keyValTable.setItems(data);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }


    }

    public void addKey(ActionEvent event)
    {
        /*
        Get the key and value from the input boxes
         */
        String k = keyInput.getText();
        String v = valInput.getText();

        /*
        Get the photo set from from the album
         */
        HashSet<Photo> pSet = album.getPhotos();

        /*
        Remove the photo that you are currently working on
         */
        pSet.remove(photo);

        /*
        Add the tag to the photo
         */
        photo.addTag(k, v);

        /*
        Re add the photo
         */
        pSet.add(photo);

        /*
        Set the album photoset to the new photo
         */
        album.setPhotos(pSet);

        /*
        Commit the new photo
         */
        Album.commitAlbum(album);

        /*
        Create a temporary arrayList
         */
        ArrayList<Pair<String,String>> tempList = new ArrayList<>();

        /*
        Add the hashstuff to it
         */

        for (Map.Entry<String, HashSet<String>> e : photo.getTags().entrySet())
        {
                e.getValue().forEach(x -> tempList.add(new Pair<>(e.getKey(), x)));
        }


        data = FXCollections.observableArrayList(tempList);

        keyCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));
        valCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getValue()));
        keyValTable.setItems(data);








    }
}

