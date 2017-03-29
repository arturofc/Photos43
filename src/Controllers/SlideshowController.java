package Controllers;

import Models.Album;
import Models.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class SlideshowController
{
    @FXML
    public ImageView slideshowImageview;

    private ArrayDeque<Photo> photos;

    public void init(Album a)
    {
        photos = new ArrayDeque<>();
        photos.addAll(a.getPhotos());

        if (!(photos.isEmpty()))
        {
            try
            {
                Image i = new Image(new FileInputStream(photos.peek().getPhotoFile()));
                slideshowImageview.setImage(i);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

    }

    public void back(ActionEvent event)
    {
        if (!(photos.isEmpty()))
        {
            try
            {
                photos.push(photos.removeLast());
                Photo p = photos.peek();


                Image i = new Image(new FileInputStream(p.getPhotoFile()));
                slideshowImageview.setImage(i);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void forward(ActionEvent event)
    {
        if (!(photos.isEmpty()))
        {
            try
            {
                photos.addLast(photos.pop());
                Photo p = photos.peek();


                Image i = new Image(new FileInputStream(p.getPhotoFile()));
                slideshowImageview.setImage(i);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
}
