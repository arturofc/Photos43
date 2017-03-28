package application;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Photo implements Serializable
{
    private String name;
    private Set<String> tags;
    private File photoFile;

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

    public void addTag(Set<String> tags)
    {
       this.tags.addAll(tags);
    }
    public void addTag(String tag)
    {
        this.tags.add(tag);
    }
    private void deleteTag(String tag)
    {
        this.tags.remove(tag);
    }
    private Set<String> getTags()
    {
        return this.tags;
    }

}
