package application;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by cal13 on 3/22/2017.
 */
public class Photo
{
    String name;
    Set<String> tags;
    File photoFile;

    public Photo(String name, Set<String> tags, String path)
    {
        this.name = name;
        this.tags = tags;
        this.photoFile = new File(path);
    }
    public Photo(String name, String path)
    {
        this.name = name;
        this.photoFile = new File(path);
    }


    public BufferedImage getImage()
    {
        BufferedImage toReturn = null;
        try
        {
            toReturn = ImageIO.read(photoFile);
        }
        catch (IOException e)
        {
            System.out.println("IO Exception : Photo " + name + " could not be found");
        }

        return toReturn;
    }

    public void updateTags(Set<String> tags)
    {
       this.tags.addAll(tags);
    }
}
