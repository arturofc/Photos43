package Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Photo implements Serializable
{
    private String name;
    private Set<String> tags;
    private File photoFile;
    LocalDate date;


    public Photo(String name, Set<String> tags, String path)
    {
        this.name = name;
        this.tags = tags;
        this.photoFile = new File(path);
        this.date = LocalDate.now();
    }

    public Photo(String name, String path)
    {
        this.name = name;
        this.tags = new TreeSet<>();
        this.photoFile = new File(path);
        this.date = LocalDate.now();
    }

    public Photo(String name, Set<String> tags, String path, LocalDate date)
    {
        this.name = name;
        this.tags = tags;
        this.photoFile = new File(path);
        this.date = date;
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

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }
}
