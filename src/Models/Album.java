package Models;


import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Album implements Serializable
{
    private String name;
    private HashSet<Photo> photos;
    private User owner;

    public Album(String name, User owner)
    {
        this.name = name;
        this.photos = new HashSet<>();
        this.owner = owner;
    }

    public Album(String name, HashSet<Photo> photos, User owner)
    {
        this.name = name;
        this.photos = photos;
        this.owner = owner;
    }

    public String getDateRange()
    {
        if (photoCount() == 0)
        {
            return "";
        }
        else
        {
            return Collections.max(getDateList()).toString() + " - " + Collections.min(getDateList()).toString();
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

    public int photoCount()
    {
        return getPhotos().size();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public HashSet<Photo> getPhotos()
    {
        return photos;
    }

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }


    public void setPhotos(HashSet<Photo> photos)
    {
        this.photos = photos;
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

    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.owner);
    }

    /*
    Static methods
    ---------------------------------------
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
