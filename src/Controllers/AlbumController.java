package Controllers;

import Models.Album;
import Models.Photo;
import Models.User;
import application.Photos;
import application.UserLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;


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

    /**
     * Initializes the Album table
     *
     * @param u the user being passed in.
     * @param a the album being passed in.
     */
    public void init(User u, Album a)
    {
        /*
        Set the global owner and album to what gets passed in
         */
        user = u;
        album = a;

        /*
        Configure the user table
         */
        imageTable.setPadding(new Insets(15, 15, 15, 15));
        imageTable.setHgap(15);



        /*
        From the global album variable, run get photos and get the set
        Create imageView and add to the imageTable
         */
        for (Photo p : album.getPhotos())
        {
            imageTable.getChildren().addAll(createImageView(p));
        }

        albumLabel.setText(album.getName());
    }

    /**
     * Global logout
     *
     * @param event the button event
     * @throws Exception throws an exception
     */
    public void logout(ActionEvent event) throws Exception
    {
        Photos.logout(event);
    }

    /**
     * Goes back to the user screen
     *
     * @param event the button event
     * @throws Exception throws an exception
     */
    public void back(ActionEvent event) throws Exception
    {
        ((Node) (event.getSource())).getScene().getWindow().hide();

        UserLauncher.start(user);
    }

    /**
     * Open the filechooser to pick the images
     *
     * @param event the button event
     */
    public void pickImage(ActionEvent event)
    {

        /*
         * Create the file chooser
         */
        FileChooser fChoose = new FileChooser();

        /*
        Create the extension filter so that they can only share JPGs and whatnot
         */
        FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fChoose.getExtensionFilters().addAll(extJPG, extPNG);

        /*
        Open the dialog chooser but set owner of this chooser to null
         */
        File f = fChoose.showOpenDialog(null);

        /*
        If f is not null, add the photo
         */
        if (f != null)
        {
            HashSet<Photo> pSet = album.getPhotos();
            pSet.add(new Photo(f.getName(), f));

            album.setPhotos(pSet);

            imageTable.getChildren().clear();

            for (Photo p : album.getPhotos())
            {
                imageTable.getChildren().addAll(createImageView(p));
            }

            Album.commitAlbum(album);
        }
    }

    private VBox createImageView(Photo p)
    {
        VBox v = new VBox();

        try
        {
            Image i = new Image(new FileInputStream(p.getPhotoFile()), 500, 500, true, true);
            Label l = new Label(p.getName());
            l.maxWidth(10);
            ImageView img = new ImageView(i);

            img.setViewport(new Rectangle2D(150, 125, 150, 100));
            img.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 0);");

            v.setSpacing(6);
            v.getChildren().add(img);
            v.getChildren().add(l);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Error creating image view");
        }
        return v;
    }

}
