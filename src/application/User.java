package application;

import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

import javax.imageio.IIOException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Created by cal13 on 3/24/2017.
 */
public class User implements Serializable
{

    private String name;
    private String username;
    private String password;
    private boolean isAdmin;

    public User(String name, String username, String password)
    {

        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    public User(String name, String username, String password, boolean isAdmin)
    {

        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * Checks if given username is an administrator
     *
     * @param username username to check if is admin.
     * @return true if admin, false if not
     */
    public static boolean isAdmin(String username)
    {
        /*
        Try opening the file Users.ser. Obviously if it cannot be opened
        then the user does not exist therefore not an admin
         */
        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            HashSet<User> uL = (HashSet<User>) o.readObject();

            /*
            Loop through set and check if username matches and if it is an admin
            If true for both return true
             */
            for (User x : uL)
            {
                if (x.getUsername().equals(username) && x.isAdmin)
                    return true;
            }
            return false;
        }
        catch (ClassNotFoundException | IOException e)
        {
            return false;
        }
    }

    /**
     * Checks if given username exists.
     *
     * @param username username to check if exists.
     * @return true if exists, false if not
     */
    public static boolean doesUsernameExist(String username)
    {
        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            HashSet<User> uL = (HashSet<User>) o.readObject();

            return uL.contains(new User("", username, ""));

        }
        catch (ClassNotFoundException | IOException e)
        {
            return false;
        }

    }

    /**
     * Check, if given some username and password if that combo exists and if it is correct
     * @param username username input
     * @param password password input
     * @return true if match, false if not
     */
    public static boolean checkUserAndPass(String username, String password)
    {
        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            HashSet<User> uL = (HashSet<User>) o.readObject();

            for (User x : uL)
            {
                if (x.getUsername().equals(username) && x.getPassword().equals(password))
                {
                    return true;
                }
            }

            return false;
        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error checking username and password");
            return false;
        }
    }

    /**
     * Commit a user to file. If user exists, it will be overwritten
     * @param u user to commit
     * @return true if commited. False if not
     */

    public static boolean commitUser(User u)
    {
        HashSet<User> uL = new HashSet<>();

        /*
        Try to pull old user file to update
        If can't assume this is the first time
         */
        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            uL = (HashSet<User>) o.readObject();


        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error pulling old Users");
        }

        /*
        If username already exists
         */
        if (uL.contains(u))
        {
            uL.remove(u);
            uL.add(u);
            System.out.println("Username already exists, overwriting old user");
        }
        else
        {
            uL.add(u);
        }



        try
        {
            FileOutputStream fo = new FileOutputStream("Users.ser");
            ObjectOutputStream oo = new ObjectOutputStream(fo);

            oo.writeObject(uL);
            return true;

        }
        catch (IOException e)
        {
            return false;
        }

    }

    /**
     * Get the name of the user
     * @return the name of the user
     */

    public String getName()
    {
        return this.name;
    }

    /**
     * Set the name of the user
     * @param name the name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Check if passwoord matches the inputted password
     * @param password to check
     * @return true if match, false if not
     */
    public boolean checkPassword(String password)
    {
        return (password.compareTo(this.password) == 0);
    }

    /**
     * Get the username
     * @return the username
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * Sets the username
     * @param username username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets the password
     * @return returns the password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Set password of this instance.
     * @param password password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Compare equality, objects are equal if they have the same username.
     *
     * @param o object to compare equality
     * @return true if equal, false if not.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof User))
            return false;

        User u = (User) o;

        return this.username.equals(u.getUsername());
    }

    public int hashCode()
    {
        return Objects.hash(this.username);
    }

    public static ArrayList<User> getUserList()
    {
        ArrayList<User> users = new ArrayList<>();

        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);


            HashSet<User> uL = (HashSet<User>) o.readObject();

            users.addAll(uL);

            return users;

        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error getting user list returning empty list");
            return users;
        }

    }
}
