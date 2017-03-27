package application;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by cal13 on 3/22/2017.
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
