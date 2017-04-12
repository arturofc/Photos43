package Models;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class Photo implements Serializable
{
    private String name;
    private HashMap<String, HashSet<String>> tags;
    private File photoFile;
    private LocalDate date;


    /**
     * The constructor
     *
     * @param name the photo name
     * @param tags the photo tags
     * @param file the photo file
     */
    public Photo(String name, HashMap<String, HashSet<String>> tags, File file)
    {
        this.name = name;
        this.tags = tags;
        this.photoFile = file;
        this.date = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Constructor
     *
     * @param name the photo
     * @param file
     */
    public Photo(String name, File file)
    {
        this.name = name;
        this.tags = new HashMap<>();
        this.photoFile = file;
        this.date = Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Constructor
     *
     * @param name the photo name
     * @param file the photo file
     * @param date the photo date
     */
    public Photo(String name, File file, LocalDate date)
    {
        this.name = name;
        this.tags = new HashMap<>();
        this.photoFile = file;
        this.date = date;
    }

    /**
     * constructor
     *
     * @param name the photo name
     * @param tags the photo tags
     * @param file the photo file
     * @param date the photo date
     */
    public Photo(String name, HashMap<String, HashSet<String>> tags, File file, LocalDate date)
    {
        this.name = name;
        this.tags = tags;
        this.photoFile = file;
        this.date = date;
    }

    /**
     * Add a bunch of tags at onces
     *
     * @param tags the tags
     */
    public void addTag(HashMap<String, HashSet<String>> tags)
    {
        this.tags.putAll(tags);
    }

    /**
     * Remove a key value pair
     *
     * @param key   the key
     * @param value the value
     */
    public void removeTag(String key, String value)
    {
        this.tags.get(key).remove(value);
    }

    /**
     * Add a tag
     *
     * @param key   the key
     * @param value the value
     */
    public void addTag(String key, String value)
    {
        if (this.tags.containsKey(key))
        {
            HashSet<String> tempSet = this.tags.get(key);
            tempSet.add(value);
            this.tags.put(key, tempSet);
        }
        else
        {
            HashSet<String> tempSet = new HashSet<>();
            tempSet.add(value);
            this.tags.put(key, tempSet);
        }
    }

    /**
     * Get all the tags
     *
     * @return the tag hashmap
     */
    public HashMap<String, HashSet<String>> getTags()
    {
        return this.tags;
    }

    /**
     * Get the photo date
     *
     * @return the localdate
     */
    public LocalDate getDate()
    {
        return date;
    }

    /**
     * get the photo file
     *
     * @return the file
     */
    public File getPhotoFile()
    {
        return this.photoFile;
    }

    /**
     * Set the photo date
     *
     * @param date the new date
     */
    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    /**
     * Get the photo name
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the photo name
     *
     * @param name the name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Override equals
     *
     * @param o the object passed in
     * @return true if equal false if not
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof Photo))
            return false;

        Photo p = (Photo) o;

        return (this.photoFile.equals(p.getPhotoFile()));
    }

    /**
     * Override the hashcode
     *
     * @return the int hashcode
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.photoFile);
    }
}
