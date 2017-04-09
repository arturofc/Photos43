package Controllers;

import Models.Album;
import Models.Photo;
import Models.User;
import application.DetailLauncher;
import application.Photos;
import application.SlideshowLauncher;
import application.UserLauncher;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;

import static application.Photos.showError;


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
    @FXML
    public MenuButton moveToMenu;
    @FXML
    public MenuButton copyToMenu;

    private User user;
    private Album album;
    //private boolean selected = false;
    private BooleanProperty selected = null;
    private Photo s = null;
    private Stage stg;

    /**
     * Initializes the Album table
     *
     * @param u the user being passed in.
     * @param a the album being passed in.
     */
    public void init(User u, Album a, Stage stage)
    {
        /*
        Set the global owner and album to what gets passed in
         */
        user = u;
        album = a;
        stg = stage;

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
            if (createTile(p) != null)
            imageTable.getChildren().addAll(createTile(p));
        }

        albumLabel.setText(album.getName());

        copyTo();
        moveTo();
        /*
        Basicaly refresh on refocus
         */
        stg.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            s = null;
            selected = null;

            imageTable.getChildren().clear();

            for (Photo p : album.getPhotos())
            {
                if (createTile(p) != null)
                imageTable.getChildren().addAll(createTile(p));
            }
        });

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
        UserLauncher.start(user, stg);
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
            LocalDate l = Instant.ofEpochMilli(f.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();
            pSet.add(new Photo(f.getName(), f, l));

            album.setPhotos(pSet);

            imageTable.getChildren().clear();

            for (Photo p : album.getPhotos())
            {
                if (createTile(p) != null)
                    imageTable.getChildren().addAll(createTile(p));
            }

            Album.commitAlbum(album);
        }
    }

    /**
     * Creates the slideshow for the album
     *
     * @param event the button event
     * @throws IOException
     */
    public void slideshow(ActionEvent event) throws IOException
    {
        if (album == null || album.getPhotos().size() == 0)
        {
            showError("Empty Album", "No photos in album", "Can't display an empty album");
            return;
        }

        SlideshowLauncher.start(album);
    }

    /**
     * Creates the tile for the photo and handles the click event and selection
     *
     * @param p the photo to pass in to create the tile
     * @return the create vbox tile
     */
    private VBox createTile(Photo p)
    {
        VBox v = new VBox();

        try
        {
            /*
            Image File
             */

            Image i;

            if (p.getPhotoFile().exists())
                i = new Image(new FileInputStream(p.getPhotoFile()), 500, 500, true, true);
            else
            {
                return null;
            }


            /*
            Photo label
             */
            Label l = new Label(p.getName());

            /*
            Label max width
             */
            l.maxWidth(10);

            /*
            Create imageview from image i, add a dropshadow and change the viewport a bit
             */
            ImageView img = new ImageView(i);
            img.setViewport(new Rectangle2D(150, 125, 150, 100));
            img.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 0);");

            /*
            Add to Vbox
             */
            v.setSpacing(6);
            v.getChildren().add(img);
            v.getChildren().add(l);

            /*
            The border
             */
            PseudoClass border = PseudoClass.getPseudoClass("b");

            v.getStyleClass().add("img-border");

            BooleanProperty borderActive = new SimpleBooleanProperty()
            {
                @Override
                protected void invalidated()
                {
                    v.pseudoClassStateChanged(border, get());
                }
            };

            /*
            Handles what happens when something is clicked
             */
            v.setOnMouseClicked(event ->
            {
                /*
                If nothing is selected and the current border is not active
                 */
                if (selected == null && !borderActive.getValue())
                {
                    borderActive.setValue(true);
                    selected = borderActive;
                    s = p;

                }
                /*
                If something is selected but the current border is not active
                 */
                else if (selected != null && !borderActive.getValue())
                {
                    selected.setValue(false);
                    selected = null;
                    borderActive.setValue(true);
                    selected = borderActive;
                    s = p;
                }
                /*
                If something is selected and the current border is active
                 */
                else if (selected != null && borderActive.getValue())
                {
                    selected.setValue(false);
                    selected = null;
                    s = null;

                }
            });

            v.setOnMouseEntered(t -> v.setStyle("-fx-background-color:#eeeeee;"));
            v.setOnMouseExited(t -> v.setStyle("-fx-background-color:transparent;"));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Error creating image view");
        }
        return v;
    }

    /**
     * Deletes a photo
     *
     * @param event the button event
     */
    public void delete(ActionEvent event)
    {
        /*
        if selected photo is null ie not selected
        should run more tests on this though
         */
        if (s == null)
        {
            showError("No Selection", "No Photo Selected", "You must select a photo to delete");

        }
        else
        {
            HashSet<Photo> pSet = album.getPhotos();
            pSet.remove(s);

            /*
            Set selected to null
             */
            s = null;
            selected = null;

            album.setPhotos(pSet);
            imageTable.getChildren().clear();

            for (Photo p : album.getPhotos())
            {
                if (createTile(p) != null)
                imageTable.getChildren().addAll(createTile(p));
            }

            Album.commitAlbum(album);
        }
    }


    public void details(ActionEvent event) throws IOException
    {
        if (imageTable.getChildren().isEmpty())
        {
            showError("Empty", "Empty Album", "Must have photos in album to show details.");
        }
        else if (s == null)
        {
            showError("No Selection", "No Photo Selection", "Please select a photo to open.");
        }
        else
        {
            DetailLauncher.start(user, album, s);
        }
    }

    public void moveTo()
    {
        for (Album x : Album.getAlbumList(user))
        {
            MenuItem m = new MenuItem(x.getName());
            m.setOnAction(event ->
            {
                if (s != null)
                {
                    x.addPhoto(s);

                    Album.commitAlbum(x);
                    album.removePhoto(s);

                    s = null;
                    selected = null;

                    imageTable.getChildren().clear();

                    for (Photo p : album.getPhotos())
                    {
                        if (createTile(p) != null)
                        imageTable.getChildren().addAll(createTile(p));
                    }

                    Album.commitAlbum(album);

                }
                else
                {
                    showError("No Selection", "No Photo Selected", "You must select a photo to move");
                }
            });

                        /*
            If the album name is not equal to the current album then add that to the move to menu list
             */
            if (!x.equals(album))
                moveToMenu.getItems().add(m);
        }
    }
    public void copyTo()
    {
        for (Album x : Album.getAlbumList(user))
        {
            MenuItem m = new MenuItem(x.getName());
            m.setOnAction(event ->
            {
                if (s != null)
                {
                    x.addPhoto(s);

                    Album.commitAlbum(x);

                    s = null;
                    selected = null;

                    imageTable.getChildren().clear();

                    for (Photo p : album.getPhotos())
                    {
                        if (createTile(p) != null)
                        imageTable.getChildren().addAll(createTile(p));
                    }

                    Album.commitAlbum(album);

                }
                else
                {
                    showError("No Selection", "No Photo Selected", "You must select a photo to move");
                }
            });

                        /*
            If the album name is not equal to the current album then add that to the move to menu list
             */
            if (!x.equals(album))
                copyToMenu.getItems().add(m);
        }
    }


}
