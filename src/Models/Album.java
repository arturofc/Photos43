package Models;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Album implements Serializable
{
    private String name;
    private HashSet<Photo> photos;
    private User owner;

    /**
     * Constructor
     *
     * @param name  the album name
     * @param owner album owner
     */
    public Album(String name, User owner)
    {
        this.name = name;
        this.photos = new HashSet<>();
        this.owner = owner;
    }

    /**
     * Constructor
     *
     * @param name   the album name
     * @param photos hashset of photos
     * @param owner  the owner
     */
    public Album(String name, HashSet<Photo> photos, User owner)
    {
        this.name = name;
        this.photos = photos;
        this.owner = owner;
    }

    /**
     * Get a string of the date range of the photos
     *
     * @return the date range string
     */
    public String getDateRange()
    {
        if (photoCount() == 0)
        {
            return "";
        }
        else
        {
            return Collections.min(getDateList()).toString() + " - " + Collections.max(getDateList()).toString();
        }
    }

    private ArrayList<LocalDate> getDateList()
    {
        ArrayList<LocalDate> d = new ArrayList<>();
        for (Photo a : getPhotos())
        {
            d.add(a.getDate());
        }

        return d;
    }

    /**
     * The number of photos
     *
     * @return int size
     */
    public int photoCount()
    {
        return getPhotos().size();
    }

    /**
     * The album name
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name
     *
     * @param name the new name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get all the photos
     *
     * @return the photos
     */
    public HashSet<Photo> getPhotos()
    {
        return photos;
    }

    /**
     * Get the owner
     *
     * @return the user owner
     */
    public User getOwner()
    {
        return owner;
    }

    /**
     * set the owner
     *
     * @param owner the owner
     */
    public void setOwner(User owner)
    {
        this.owner = owner;
    }


    /**
     * Set all the photos
     *
     * @param photos the photos
     */
    public void setPhotos(HashSet<Photo> photos)
    {
        this.photos = photos;
    }

    /**
     * Add a photo
     *
     * @param p the photo
     * @return true if added false if not
     */
    public boolean addPhoto(Photo p)
    {
        return this.photos.add(p);
    }

    /**
     * Remove a photo
     *
     * @param p photo to remove
     * @return true if removed false if not
     */
    public boolean removePhoto(Photo p)
    {
        return this.photos.remove(p);
    }

    /**
     * Compare equality, objects are equal if they have the same album name and same owner.
     *
     * @param o object to compare equality
     * @return true if equal, false if not.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof Album))
            return false;

        Album a = (Album) o;

        return (this.name.equals(a.getName()) && this.owner.equals(a.getOwner()));
    }

    /**
     * The hashcode for album
     *
     * @return hashcode int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.owner);
    }

    /*
    Static methods
    ---------------------------------------
     */

    /**
     * All the albums for a user
     *
     * @param u the user
     * @return an arraylist of all the albums
     */
    public static ArrayList<Album> getAlbumList(User u)
    {
        ArrayList<Album> albums = new ArrayList<>();

        try
        {
            FileInputStream f = new FileInputStream("Albums.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            HashSet<Album> aL = (HashSet<Album>) o.readObject();

            for (Album a : aL)
            {
                if (a.getOwner().equals(u))
                    albums.add(a);
            }

            return albums;

        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error getting album list for User u, returning empty list");
            return albums;
        }
    }

    /**
     * Save an album to disk
     *
     * @param a the album
     * @return true if saved
     */
    public static boolean commitAlbum(Album a)
    {
        HashSet<Album> aL = new HashSet<>();

        /*
        Try to pull old user file to update
        If can't assume this is the first time
         */
        try
        {
            FileInputStream f = new FileInputStream("Albums.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            aL = (HashSet<Album>) o.readObject();


        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error pulling old Albums " + e.toString());
        }

        /*
        If username already exists
         */
        if (aL.contains(a))
        {
            aL.remove(a);
            aL.add(a);
            System.out.println("Album already exists, overwriting old album");
        }
        else
        {
            aL.add(a);
        }


        try
        {
            FileOutputStream fo = new FileOutputStream("Albums.ser");
            ObjectOutputStream oo = new ObjectOutputStream(fo);

            oo.writeObject(aL);
            return true;

        }
        catch (IOException e)
        {
            return false;
        }
    }

    /**
     * Does the album exist
     *
     * @param name the album name
     * @param u    the owner
     * @return true if exists false if no
     */
    public static boolean doesAlbumNameExist(String name, User u)
    {
        try
        {
            FileInputStream f = new FileInputStream("Albums.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            HashSet<Album> uL = (HashSet<Album>) o.readObject();

            return uL.contains(new Album(name, u));

        }
        catch (ClassNotFoundException | IOException e)
        {
            return false;
        }
    }

    /**
     * Rename an album
     *
     * @param a       album
     * @param u       the owner
     * @param newName the new name
     * @return true if renamed false if not
     */
    public static boolean renameAlbum(Album a, User u, String newName)
    {
        if (doesAlbumNameExist(newName, u))
            return false;
        else if (doesAlbumNameExist(a.getName(), a.getOwner()))
        {
            ArrayList<Album> aList = Album.getAlbumList(u);
            Album oldAlbum;

            for (Album x : aList)
            {
                if (x.equals(a))
                {
                    oldAlbum = x;
                    Album.deleteAlbum(x);
                    oldAlbum.setName(newName);
                    return (Album.commitAlbum(oldAlbum));
                }
            }
            return false;

        }
        else
            return false;
    }

    /**
     * Delete an album
     *
     * @param a the album
     * @return true if deleted false if not
     */
    public static boolean deleteAlbum(Album a)
    {
        HashSet<Album> aL = new HashSet<>();

        /*
        If you cannot retrieve the file you cannot remove the user
         */
        try
        {
            FileInputStream f = new FileInputStream("Albums.ser");
            ObjectInputStream o = new ObjectInputStream(f);

            aL = (HashSet<Album>) o.readObject();

        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error removing album (Opening album list) " + e.toString());
            return false;
        }

        if (aL.contains(a))
        {
            aL.remove(a);

        }

        try
        {
            FileOutputStream fo = new FileOutputStream("Albums.ser");
            ObjectOutputStream oo = new ObjectOutputStream(fo);

            oo.writeObject(aL);
            return true;

        }
        catch (IOException e)
        {
            System.out.println("Error storing updated album list while trying to remove " + e.toString());
            return false;
        }
    }

}
