package Models;

import Models.Photo;
import Models.User;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Album
{
    private String name;
    private Set<Photo> photos;
    private User owner;

    public Album(String name)
    {
        this.name = name;
        this.photos = new TreeSet<>();
    }

    public Album(String name, Set<Photo> photos)
    {
        this.name = name;
        this.photos = photos;
    }


}
