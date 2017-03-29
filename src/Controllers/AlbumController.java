package Controllers;

import Models.Album;
import Models.Photo;
import Models.User;
import application.Photos;
import application.UserLauncher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class AlbumController
{
    @FXML
    public Label albumLabel;
    @FXML
    public TilePane imageTable;

    private User user;
    private Album album;

    public void init(User u, Album a)
    {
        user = u;
        album = a;

        imageTable.setPadding(new Insets(15, 15, 15, 15));
        imageTable.setHgap(15);

        for (Photo p : album.getPhotos())
        {
            ImageView imageView;
            imageView = createImageView(p.getPhotoFile());
            imageTable.getChildren().addAll(imageView);
        }

        albumLabel.setText(album.getName());
    }

    public void logout(ActionEvent event) throws Exception
    {
        Photos.logout(event);
    }

    public void back(ActionEvent event) throws Exception
    {
        ((Node) (event.getSource())).getScene().getWindow().hide();

        UserLauncher.start(user);
    }

    public void pickImage(ActionEvent event)
    {
        final FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        File file = fileChooser.showOpenDialog(null);

        if (file != null)
        {
            HashSet<Photo> pSet = album.getPhotos();
            pSet.add(new Photo(file.getName(), file));

            album.setPhotos(pSet);

            imageTable.getChildren().clear();

            for (Photo p : album.getPhotos())
            {
                ImageView imageView;
                imageView = createImageView(p.getPhotoFile());
                imageTable.getChildren().addAll(imageView);
            }

            Album.commitAlbum(album);
        }
    }

    private ImageView createImageView(final File imageFile)
    {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try
        {
            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);
            imageView = new ImageView(image);
            imageView.setFitWidth(150);

        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        return imageView;
    }
}
