package Models;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Calin Gilan
 * @author Arturo Corro
 */
public class User implements Serializable
{

    private String name;
    private String username;
    private String password;
    private boolean isAdmin;

    /**
     * User constructor
     *
     * @param name     display name
     * @param username username
     * @param password password
     */
    public User(String name, String username, String password)
    {

        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    /**
     * User constructor
     *
     * @param name     display name
     * @param username username
     * @param password password
     * @param isAdmin  if user is admin or not
     */
    public User(String name, String username, String password, boolean isAdmin)
    {

        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    /**
     * Get the name of the user
     *
     * @return the name of the user
     */

    public String getName()
    {
        return this.name;
    }

    /**
     * Set the name of the user
     *
     * @param name the name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Check if passwoord matches the inputted password
     *
     * @param password to check
     * @return true if match, false if not
     */
    public boolean checkPassword(String password)
    {
        return (password.compareTo(this.password) == 0);
    }

    /**
     * Get the username
     *
     * @return the username
     */
    public String getUsername()
    {
        return this.username;
    }

    /**
     * Sets the username
     *
     * @param username username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets the password
     *
     * @return returns the password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Set password of this instance.
     *
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

    /**
     * Override hashcode for users. Users have same hashcode if they have the same username
     *
     * @return hashcode for user
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.username);
    }

    /*
    -------------------------------------
    Static Methods
    */

    /**
     * Get stored user list
     *
     * @return list of all users
     */
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
     * Commit a user to file. If user exists, it will be overwritten
     *
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
            System.out.println("Error pulling old Users " + e.toString());
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
     * Check, if given some username and password if that combo exists and if it is correct
     *
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
            System.out.println("Error checking username and password " + e.toString());
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
     * Given a user, delete it
     *
     * @param u user to delete
     * @return true if deleted, false if some kind of error
     */
    public static boolean deleteUser(User u)
    {
        HashSet<User> uL = new HashSet<>();

        /*
        If you cannot retrieve the file you cannot remove the user
         */
        try
        {
            FileInputStream f = new FileInputStream("Users.ser");
            ObjectInputStream o = new ObjectInputStream(f);

            uL = (HashSet<User>) o.readObject();

        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error removing user " + e.toString());
            return false;
        }

        if (uL.contains(u))
        {
            uL.remove(u);

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
            System.out.println("Error storing updated user list while trying to remove " + e.toString());
            return false;
        }
    }

    /**
     * Given a username, return a user
     *
     * @param username username to get
     * @return Optional which contains a user if the user exists
     */
    public static Optional<User> getUser(String username)
    {
        if (doesUsernameExist(username))
        {
            try
            {
                FileInputStream f = new FileInputStream("Users.ser");
                ObjectInputStream o = new ObjectInputStream(f);


                HashSet<User> uL = (HashSet<User>) o.readObject();

                for (User x : uL)
                {
                    if (x.getUsername().equals(username))
                    {
                        return Optional.of(x);
                    }
                }

                return Optional.empty();
            }
            catch (ClassNotFoundException | IOException e)
            {
                System.out.println("Error getting and returning user: " + e.toString());
                return Optional.empty();
            }
        }
        else
        {
            System.out.println("Username does not exist");
            return Optional.empty();
        }
    }
}
