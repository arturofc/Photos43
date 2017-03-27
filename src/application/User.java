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


    public String getName()
    {
        return this.name;
    }

    public boolean checkPassword(String password)
    {
        return (password.compareTo(this.password) == 0);
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }


    /**
     * Checks if given username is an administrator
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

            return uL.contains(new User("",username,""));

        }
        catch (ClassNotFoundException | IOException e)
        {
            return false;
        }

    }

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
            return false;
        }
    }

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


        if (uL.contains(u))
        {
            System.out.println("Username already exists");
            return false;
        }

        uL.add(u);

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

    private String getPassword()
    {
        return this.password;
    }

    /**
     * Compare equality, objects are equal if they have the same username.
     * @param o object to compare equality
     * @return true if equal, false if not.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (! (o instanceof User))
            return false;

        User u = (User) o;

        return this.username.equals(u.getUsername());
    }

    public int hashCode()
    {
        return Objects.hash(this.username);
    }
}
