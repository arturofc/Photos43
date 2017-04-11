package Controllers;

import Models.Album;
import Models.Photo;
import Models.User;
import application.DetailLauncher;
import application.Photos;
import application.SlideshowLauncher;
import application.UserLauncher;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
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
import java.util.ArrayList;
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
        updateTiles(true);

        albumLabel.setText(album.getName());

        copyTo();
        moveTo();
        /*
        Basicaly refresh on refocus
         */
        stg.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue)
            {
                updateTiles(false);
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
            /*
            Get the photos
             */
            HashSet<Photo> pSet = album.getPhotos();

            /*
            Get the last modified date
             */
            LocalDate l = Instant.ofEpochMilli(f.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();

            /*
            Add a new photo using the get name, the file pointer and the date
             */
            pSet.add(new Photo(f.getName(), f, l));

            /*
            Set the album photos
             */
            album.setPhotos(pSet);

            updateTiles(false);

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

            /* check if image file actually still exists */
            if (p.getPhotoFile().exists())
                i = new Image(new FileInputStream(p.getPhotoFile()), 500, 500, true, true);
            else
            {
                /* get current album and all the photos */
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

            album.setPhotos(pSet);

            updateTiles(false);

            Album.commitAlbum(album);
        }
    }


    /**
     * Detail panel where you add keys
     * @param event the button event
     * @throws IOException the exception that can get thrown
     */
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

    /**
     * Move to an album
     */
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

                    updateTiles(false);

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

    /**
     * Copy to an album
     */
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

                    updateTiles(false);

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

    /**
     * Updates the tiles in the background
     * @param showError whether or not to show a missing photo error
     */
    public void updateTiles(Boolean showError)
    {
        HashSet<Photo> pSet = album.getPhotos();


        new Thread(() ->
        {
            s = null;
            selected = null;
            ArrayList<Node> tImgList = new ArrayList<>();
            for (Photo p : pSet)
            {
                if (createTile(p) != null)
                    tImgList.add(createTile(p));

            }

            Platform.runLater(() ->
            {
                imageTable.getChildren().clear();
                imageTable.getChildren().addAll(tImgList);

                if (showError)
                {
                    for (Photo p: pSet)
                    {
                        if (!(p.getPhotoFile().exists()))
                            showError("Photo not found", "Photo not found", p.getPhotoFile().getName() + " could not be found. Please re-add to album");
                    }
                }
            });
        }).start();
    }


}
